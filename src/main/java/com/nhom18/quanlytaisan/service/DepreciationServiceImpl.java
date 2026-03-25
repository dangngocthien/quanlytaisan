package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.entity.DepreciationHistory;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import com.nhom18.quanlytaisan.repository.DepreciationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation cho quản lý khấu hao tài sản
 */
@Service
public class DepreciationServiceImpl implements DepreciationService {

    @Autowired
    private DepreciationHistoryRepository depreciationHistoryRepository;

    @Autowired
    private AssetRepository assetRepository;

    /**
     * Lấy lịch sử khấu hao của một tài sản
     */
    @Override
    public List<DepreciationHistoryDTO> getHistory(Long assetId) {
        return depreciationHistoryRepository
                .findByAsset_Id(assetId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Tính khấu hao hàng tháng cho một tài sản
     * 
     * Logic:
     *   Công thức: Tiền khấu hao/tháng = Giá gốc / Số tháng thuờn đơn vị (defaultUsefulLifeMonths)
     *   Cập nhật: currentValue = currentValue - tiền khấu hao
     *   Lưu: Ghi vào DepreciationHistory
     *   Cập nhật: Lại Asset trong DB
     */
    @Override
    @Transactional
    public DepreciationHistoryDTO calculateMonthlyDepreciation(Long assetId, int month, int year) {
        // 1. Lấy Asset
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Tài sản không tồn tại với ID: " + assetId));

        // 2. Kiểm tra Asset có Category không
        if (asset.getCategory() == null) {
            throw new RuntimeException("Tài sản không có danh mục, không thể tính khấu hao");
        }

        // 3. Lấy defaultUsefulLifeMonths
        Integer usefulLifeMonths = asset.getCategory().getDefaultUsefulLifeMonths();
        if (usefulLifeMonths == null || usefulLifeMonths <= 0) {
            throw new RuntimeException("Danh mục không có thông tin tuổi thọ, không thể tính khấu hao");
        }

        // 4. Kiểm tra xem tháng năm này đã tính khấu hao chưa
        var existing = depreciationHistoryRepository
                .findByAsset_IdAndPeriodMonthAndPeriodYear(assetId, month, year);
        if (existing.isPresent()) {
            throw new RuntimeException("Đã tính khấu hao cho tài sản này trong tháng " + month + "/" + year);
        }

        // 5. Tính tiền khấu hao 1 tháng = Giá trị sản phẩm / Tuổi thọ
        BigDecimal purchasePrice = asset.getPurchasePrice(); // Giá trị của sản phẩm
        BigDecimal depreciationAmount = purchasePrice
                .divide(new BigDecimal(usefulLifeMonths), 2, RoundingMode.HALF_UP);

        // 6. Tính tổng khấu hao lũy tích đến hiện tại
        BigDecimal totalAccumulatedSoFar = depreciationHistoryRepository.findByAsset_Id(assetId)
                .stream()
                .map(DepreciationHistory::getDepreciationAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Giá trị còn lại = Giá mua - Khấu hao lũy tích (bao gồm cả tháng này)
        BigDecimal newAccumulatedDepreciation = totalAccumulatedSoFar.add(depreciationAmount);
        BigDecimal remainingValue = purchasePrice.subtract(newAccumulatedDepreciation);
        
        // Đảm bảo giá trị còn lại không âm
        if (remainingValue.compareTo(BigDecimal.ZERO) < 0) {
            depreciationAmount = purchasePrice.subtract(totalAccumulatedSoFar); // khấu hao nốt phần còn lại
            remainingValue = BigDecimal.ZERO;
        }

        // 7. Tạo bản ghi DepreciationHistory
        DepreciationHistory depreciationHistory = new DepreciationHistory();
        depreciationHistory.setAsset(asset);
        depreciationHistory.setPeriodMonth(month);
        depreciationHistory.setPeriodYear(year);
        depreciationHistory.setDepreciationAmount(depreciationAmount);
        depreciationHistory.setRemainingValue(remainingValue);
        depreciationHistory.setNotes("Khấu hao tháng " + month + "/" + year + ", công thức: " + purchasePrice + " / " + usefulLifeMonths);
        depreciationHistory.setCalculatedAt(LocalDateTime.now());
        depreciationHistory.setCreatedAt(LocalDateTime.now());

        // 8. LƯU vào DATABASE
        DepreciationHistory saved = depreciationHistoryRepository.save(depreciationHistory);

        // 9. CẬP NHẬT Asset với giá trị mới
        asset.setCurrentValue(remainingValue);
        asset.setUpdatedAt(LocalDateTime.now());
        assetRepository.save(asset);

        // 10. Trả về DTO
        return convertToDTO(saved);
    }

    /**
     * Lấy chi tiết một bản ghi khấu hao theo ID
     */
    @Override
    public DepreciationHistoryDTO getDepreciationById(Long id) {
        return depreciationHistoryRepository
                .findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Bản ghi khấu hao không tồn tại với ID: " + id));
    }

    /**
     * Tính khấu hao cho tất cả tài sản trong một tháng
     */
    @Override
    @Transactional
    public List<DepreciationHistoryDTO> calculateAllAssetsDepreciation(int month, int year) {
        // Lấy tất cả tài sản
        List<Asset> allAssets = assetRepository.findAll();
        
        List<DepreciationHistoryDTO> results = allAssets.stream()
                .map(asset -> {
                    try {
                        return calculateMonthlyDepreciation(asset.getId(), month, year);
                    } catch (RuntimeException e) {
                        // Bỏ qua tài sản không thể tính khấu hao (đã tính, không có category, v.v.)
                        System.err.println("Lỗi tính khấu hao cho asset " + asset.getId() + ": " + e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
        
        return results;
    }

    /**
     * Helper method: Convert Entity sang DTO
     */
    private DepreciationHistoryDTO convertToDTO(DepreciationHistory entity) {
        DepreciationHistoryDTO dto = new DepreciationHistoryDTO();
        dto.setId(entity.getId());
        
        if (entity.getAsset() != null) {
            dto.setAssetId(entity.getAsset().getId());
            dto.setAssetName(entity.getAsset().getName());
        }
        
        dto.setPeriodMonth(entity.getPeriodMonth());
        dto.setPeriodYear(entity.getPeriodYear());
        dto.setDepreciationAmount(entity.getDepreciationAmount());
        dto.setRemainingValue(entity.getRemainingValue());
        dto.setNotes(entity.getNotes());
        dto.setCalculatedAt(entity.getCalculatedAt());
        
        return dto;
    }
}
