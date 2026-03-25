package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetDTO;
import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.entity.DepreciationHistory;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import com.nhom18.quanlytaisan.repository.DepreciationHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation cho quản lý báo cáo
 * Phase 3: Basic asset reports
 * Phase 4: Depreciation & Valuation reports (Mới)
 *
 * Áp dụng senior Spring Boot patterns:
 * - Constructor injection (không dùng @Autowired trên field)
 * - Manual Entity → DTO mapping
 * - Transactional consistency
 */
@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    // ============ DEPENDENCIES (Constructor Injection) ============
    private final AssetRepository assetRepository;
    private final DepreciationHistoryService depreciationHistoryService;
    private final DepreciationHistoryRepository depreciationHistoryRepository;

    /**
     * Constructor injection - Senior Spring Boot practice
     * Spring tự động @Autowired constructor này
     */
    public ReportServiceImpl(
            AssetRepository assetRepository,
            DepreciationHistoryService depreciationHistoryService,
            DepreciationHistoryRepository depreciationHistoryRepository) {
        this.assetRepository = assetRepository;
        this.depreciationHistoryService = depreciationHistoryService;
        this.depreciationHistoryRepository = depreciationHistoryRepository;
    }

    // ============ PHASE 3: BASIC ASSET REPORTS ============

    @Override
    public List<AssetDTO> getAssetsByDepartment(Long departmentId) {
        return assetRepository
                .findAll()
                .stream()
                .filter(asset -> asset.getCurrentDepartment() != null && 
                        asset.getCurrentDepartment().getId().equals(departmentId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetDTO> getAssetsByCategory(Long categoryId) {
        return assetRepository
                .findAll()
                .stream()
                .filter(asset -> asset.getCategory() != null && 
                        asset.getCategory().getId().equals(categoryId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetDTO> getAssetsByStatus(String status) {
        return assetRepository
                .findAll()
                .stream()
                .filter(asset -> asset.getStatus() != null && 
                        asset.getStatus().equalsIgnoreCase(status))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetDTO> getAllAssets() {
        return assetRepository
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Double getTotalValueByDepartment(Long departmentId) {
        return assetRepository
                .findAll()
                .stream()
                .filter(asset -> asset.getCurrentDepartment() != null && 
                        asset.getCurrentDepartment().getId().equals(departmentId))
                .map(Asset::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }

    @Override
    public Double getTotalValueByCategory(Long categoryId) {
        return assetRepository
                .findAll()
                .stream()
                .filter(asset -> asset.getCategory() != null && 
                        asset.getCategory().getId().equals(categoryId))
                .map(Asset::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }

    // ============ PHASE 4: DEPRECIATION & VALUATION REPORTS ============

    @Override
    public List<DepreciationHistoryDTO> getAssetDepreciationHistory(Long assetId) {
        return depreciationHistoryService.getDepreciationsByAsset(assetId);
    }

    @Override
    public BigDecimal getAssetBookValue(Long assetId) {
        return depreciationHistoryService.getCurrentBookValue(assetId);
    }

    @Override
    public Map<String, Object> getAssetValuationReport(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found: " + assetId));

        BigDecimal purchasePrice = asset.getPurchasePrice();
        BigDecimal currentValue = asset.getCurrentValue();
        BigDecimal accumulatedDepreciation = depreciationHistoryService.getTotalAccumulatedDepreciation(assetId);
        BigDecimal bookValue = depreciationHistoryService.getCurrentBookValue(assetId);

        // Tính tỷ lệ khấu hao (Accumulated Depreciation / Purchase Price * 100)
        BigDecimal depreciationRate = purchasePrice.compareTo(BigDecimal.ZERO) > 0 ?
                accumulatedDepreciation
                        .divide(purchasePrice, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("assetId", asset.getId());
        report.put("assetCode", asset.getAssetCode());
        report.put("assetName", asset.getName());
        report.put("purchasePrice", purchasePrice);
        report.put("currentSystemValue", currentValue);
        report.put("accumulatedDepreciation", accumulatedDepreciation);
        report.put("bookValue", bookValue);
        report.put("depreciationRate", depreciationRate.toString() + "%");
        report.put("purchaseDate", asset.getPurchaseDate());
        report.put("usageStartDate", asset.getUsageStartDate());
        report.put("status", asset.getStatus());
        report.put("categoryName", asset.getCategory() != null ? asset.getCategory().getName() : "N/A");

        return report;
    }

    @Override
    public List<Map<String, Object>> getDepartmentValuationReport(Long departmentId) {
        return getAssetsByDepartment(departmentId)
                .stream()
                .map(assetDTO -> getAssetValuationReport(assetDTO.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getAllValuationReports() {
        return assetRepository.findAll()
                .stream()
                .map(asset -> getAssetValuationReport(asset.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalBookValueByDepartment(Long departmentId) {
        return assetRepository
                .findAll()
                .stream()
                .filter(asset -> asset.getCurrentDepartment() != null &&
                        asset.getCurrentDepartment().getId().equals(departmentId))
                .map(asset -> depreciationHistoryService.getCurrentBookValue(asset.getId()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<Integer, Map<String, Object>> getDepreciationSummaryByYear(Integer year) {
        Map<Integer, Map<String, Object>> summary = new TreeMap<>();

        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            List<DepreciationHistory> monthRecords = depreciationHistoryRepository
                    .findAll()
                    .stream()
                    .filter(dh -> dh.getPeriodYear().equals(year) && dh.getPeriodMonth() == currentMonth)
                    .collect(Collectors.toList());

            if (!monthRecords.isEmpty()) {
                BigDecimal totalDepreciation = monthRecords
                        .stream()
                        .map(DepreciationHistory::getDepreciationAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalRemainingValue = monthRecords
                        .stream()
                        .map(DepreciationHistory::getRemainingValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<String, Object> monthSummary = new LinkedHashMap<>();
                monthSummary.put("month", month);
                monthSummary.put("recordCount", monthRecords.size());
                monthSummary.put("totalDepreciation", totalDepreciation);
                monthSummary.put("totalRemainingValue", totalRemainingValue);
                monthSummary.put("records", monthRecords.stream()
                        .map(this::mapEntityToDTO)
                        .collect(Collectors.toList()));

                summary.put(month, monthSummary);
            }
        }

        return summary;
    }

    // ============ HELPER METHODS ============

    /**
     * Map DepreciationHistory Entity sang DTO
     * Sử dụng manual mapping theo senior Spring Boot practice
     */
    private DepreciationHistoryDTO mapEntityToDTO(DepreciationHistory entity) {
        if (entity == null) {
            return null;
        }

        return new DepreciationHistoryDTO(
                entity.getId(),
                entity.getAsset().getId(),
                entity.getAsset().getAssetCode(),
                entity.getAsset().getName(),
                entity.getPeriodMonth(),
                entity.getPeriodYear(),
                entity.getDepreciationAmount(),
                entity.getRemainingValue(),
                entity.getAsset().getPurchasePrice(),
                entity.getNotes(),
                entity.getCalculatedAt(),
                entity.getCreatedAt()
        );
    }

    /**
     * Helper method: Convert Asset Entity sang AssetDTO
     */
    private AssetDTO convertToDTO(Asset entity) {
        AssetDTO dto = new AssetDTO();
        dto.setId(entity.getId());
        dto.setAssetCode(entity.getAssetCode());
        dto.setQrCodeText(entity.getQrCodeText());
        dto.setName(entity.getName());
        dto.setPurchasePrice(entity.getPurchasePrice());
        dto.setCurrentValue(entity.getCurrentValue());
        dto.setPurchaseDate(entity.getPurchaseDate());
        dto.setUsageStartDate(entity.getUsageStartDate());
        dto.setStatus(entity.getStatus());
        dto.setWarrantyProvider(entity.getWarrantyProvider());
        dto.setWarrantyExpiryDate(entity.getWarrantyExpiryDate());

        // Category Info
        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategoryName(entity.getCategory().getName());
        }

        // Current Department Info
        if (entity.getCurrentDepartment() != null) {
            dto.setCurrentDepartmentId(entity.getCurrentDepartment().getId());
            dto.setCurrentDepartmentName(entity.getCurrentDepartment().getName());
        }

        // Current Employee Info
        if (entity.getCurrentEmployee() != null) {
            dto.setCurrentEmployeeId(entity.getCurrentEmployee().getId());
            dto.setCurrentEmployeeName(entity.getCurrentEmployee().getFullName());
        }

        return dto;
    }
}
