package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.entity.DepreciationHistory;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import com.nhom18.quanlytaisan.repository.DepreciationHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation cho tính toán khấu hao tài sản
 * Phase 4: Depreciation calculation engine
 *
 * Áp dụng senior Spring Boot patterns:
 * - Constructor injection (không dùng @Autowired trên field)
 * - Manual Entity → DTO mapping
 * - Service layer validation
 * - Transactional consistency
 */
@Service
@Transactional
public class DepreciationHistoryServiceImpl implements DepreciationHistoryService {

    // ============ CONSTANTS ============
    private static final int DEFAULT_USEFUL_LIFE_MONTHS = 36; // 3 năm (fallback)
    private static final int SCALE = 2; // Làm tròn đến 2 chữ số thập phân
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    // ============ DEPENDENCIES (Constructor Injection) ============
    private final DepreciationHistoryRepository depreciationHistoryRepository;
    private final AssetRepository assetRepository;

    /**
     * Constructor injection - Senior Spring Boot practice
     * Spring tự động @Autowired constructor này
     */
    public DepreciationHistoryServiceImpl(
            DepreciationHistoryRepository depreciationHistoryRepository,
            AssetRepository assetRepository) {
        this.depreciationHistoryRepository = depreciationHistoryRepository;
        this.assetRepository = assetRepository;
    }

    // ============ CORE METHODS ============

    @Override
    public DepreciationHistoryDTO calculateDepreciation(Long assetId, Integer month, Integer year) {
        // Validation
        validatePeriodParameters(month, year);

        // Get asset
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + assetId));

        // Calculate depreciation based on original purchase price
        BigDecimal bookValue = asset.getPurchasePrice(); // Lấy giá trị của sản phẩm

        // Check if depreciation already exists for this period
        Optional<DepreciationHistory> existingDH = depreciationHistoryRepository
                .findByAsset_IdAndPeriodMonthAndPeriodYear(assetId, month, year);

        DepreciationHistory depreciationHistory;

        if (existingDH.isPresent()) {
            // Update existing record
            depreciationHistory = existingDH.get();
        } else {
            // Create new record
            depreciationHistory = new DepreciationHistory();
            depreciationHistory.setAsset(asset);
            depreciationHistory.setPeriodMonth(month);
            depreciationHistory.setPeriodYear(year);
        }

        // Determine useful life: 1) Asset, 2) Category, 3) Default
        int usefulLife = DEFAULT_USEFUL_LIFE_MONTHS;
        if (asset.getUsefulLifeMonths() != null && asset.getUsefulLifeMonths() > 0) {
            usefulLife = asset.getUsefulLifeMonths();
        } else if (asset.getCategory() != null && asset.getCategory().getDefaultUsefulLifeMonths() != null && asset.getCategory().getDefaultUsefulLifeMonths() > 0) {
            usefulLife = asset.getCategory().getDefaultUsefulLifeMonths();
        }

        // Calculate depreciation amount based on full asset value and useful life
        BigDecimal monthlyDepreciation = calculateMonthlyDepreciation(
                bookValue,
                usefulLife
        );

        // Tính tổng khấu hao lũy tích đến hiện tại (nếu đang update thì trừ đi phần cũ của tháng này)
        BigDecimal totalAccumulatedSoFar = getTotalAccumulatedDepreciation(assetId);
        if (existingDH.isPresent() && existingDH.get().getDepreciationAmount() != null) {
            totalAccumulatedSoFar = totalAccumulatedSoFar.subtract(existingDH.get().getDepreciationAmount());
        }

        // Giá trị còn lại = Giá mua - (Khấu hao lũy tích cũ + Khấu hao tháng này)
        BigDecimal newAccumulatedDepreciation = totalAccumulatedSoFar.add(monthlyDepreciation);
        BigDecimal remainingValue = bookValue.subtract(newAccumulatedDepreciation);

        // Prevent negative book value and correct the final depreciation span
        if (remainingValue.compareTo(BigDecimal.ZERO) < 0) {
            monthlyDepreciation = bookValue.subtract(totalAccumulatedSoFar); // Khấu hao khoản còn lại cuối cùng
            remainingValue = BigDecimal.ZERO;
        }

        // Set values
        depreciationHistory.setDepreciationAmount(monthlyDepreciation);
        depreciationHistory.setRemainingValue(remainingValue);
        depreciationHistory.setCalculatedAt(LocalDateTime.now());

        // Update Asset current value
        asset.setCurrentValue(remainingValue);
        assetRepository.save(asset);

        // Save to database
        DepreciationHistory saved = depreciationHistoryRepository.save(depreciationHistory);

        // Return DTO
        return mapEntityToDTO(saved);
    }

    @Override
    public BigDecimal getCurrentBookValue(Long assetId) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + assetId));

        BigDecimal totalAccumulated = getTotalAccumulatedDepreciation(assetId);
        BigDecimal bookValue = asset.getPurchasePrice().subtract(totalAccumulated);

        // Prevent negative book value
        return bookValue.abs();
    }

    @Override
    public BigDecimal getTotalAccumulatedDepreciation(Long assetId) {
        List<DepreciationHistory> histories = depreciationHistoryRepository.findByAsset_Id(assetId);

        return histories.stream()
                .map(DepreciationHistory::getDepreciationAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<DepreciationHistoryDTO> getDepreciationsByAsset(Long assetId) {
        List<DepreciationHistory> histories = depreciationHistoryRepository.findByAsset_Id(assetId);

        // Sort by year DESC, then month DESC (newest first)
        return histories.stream()
                .sorted((a, b) -> {
                    int yearCompare = b.getPeriodYear().compareTo(a.getPeriodYear());
                    if (yearCompare != 0) {
                        return yearCompare;
                    }
                    return b.getPeriodMonth().compareTo(a.getPeriodMonth());
                })
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DepreciationHistoryDTO> getDepreciationByPeriod(Long assetId, Integer month, Integer year) {
        return depreciationHistoryRepository
                .findByAsset_IdAndPeriodMonthAndPeriodYear(assetId, month, year)
                .map(this::mapEntityToDTO);
    }

    @Override
    public List<DepreciationHistoryDTO> getDepreciationsByYear(Integer year) {
        List<DepreciationHistory> histories = depreciationHistoryRepository.findAll()
                .stream()
                .filter(dh -> dh.getPeriodYear().equals(year))
                .collect(Collectors.toList());

        return histories.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteDepreciation(Long depreciationId) {
        if (depreciationHistoryRepository.existsById(depreciationId)) {
            depreciationHistoryRepository.deleteById(depreciationId);
            return true;
        }
        return false;
    }

    @Override
    public long deleteDepreciationsForAsset(Long assetId) {
        List<DepreciationHistory> histories = depreciationHistoryRepository.findByAsset_Id(assetId);
        depreciationHistoryRepository.deleteAll(histories);
        return histories.size();
    }

    @Override
    public DepreciationHistoryDTO getDepreciationById(Long depreciationId) {
        DepreciationHistory entity = depreciationHistoryRepository.findById(depreciationId)
                .orElseThrow(() -> new RuntimeException("Depreciation record not found with ID: " + depreciationId));
        return mapEntityToDTO(entity);
    }

    @Override
    public List<DepreciationHistoryDTO> calculateAllAssetsDepreciation(Integer month, Integer year) {
        validatePeriodParameters(month, year);

        List<Asset> allAssets = assetRepository.findAll();

        return allAssets.stream()
                .map(asset -> {
                    try {
                        return calculateDepreciation(asset.getId(), month, year);
                    } catch (RuntimeException e) {
                        // Bỏ qua tài sản không thể tính khấu hao (không có category, đã tính rồi, v.v.)
                        System.err.println("Lỗi tính khấu hao cho asset " + asset.getId() + ": " + e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    // ============ HELPER METHODS (Private) ============

    /**
     * Tính khấu hao hàng tháng
     * Công thức: Giá mua / Tuổi thọ (tháng)
     *
     * @param purchasePrice Giá mua
     * @param usefulLifeMonths Tuổi thọ hữu ích (tháng)
     * @return Khấu hao hàng tháng
     */
    private BigDecimal calculateMonthlyDepreciation(BigDecimal purchasePrice, Integer usefulLifeMonths) {
        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        int months = usefulLifeMonths != null && usefulLifeMonths > 0 ? usefulLifeMonths : DEFAULT_USEFUL_LIFE_MONTHS;
        return purchasePrice
                .divide(
                        new BigDecimal(months),
                        SCALE,
                        ROUNDING_MODE
                );
    }

    /**
     * Lấy giá trị sổ sách kỳ trước
     * - Nếu có kỳ trước, lấy remaining_value của kỳ đó
     * - Nếu không có, trả về giá mua (kỳ đầu tiên)
     *
     * @param assetId ID tài sản
     * @param month Tháng hiện tại
     * @param year Năm hiện tại
     * @return Giá trị sổ sách kỳ trước
     */
    private BigDecimal getPreviousPeriodRemainingValue(Long assetId, Integer month, Integer year) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found with ID: " + assetId));

        int previousMonth = month - 1;
        int previousYear = year;

        // Adjust year if month is January
        if (previousMonth < 1) {
            previousMonth = 12;
            previousYear = year - 1;
        }

        // Try to find previous period
        Optional<DepreciationHistory> previousPeriod = depreciationHistoryRepository
                .findByAsset_IdAndPeriodMonthAndPeriodYear(assetId, previousMonth, previousYear);

        if (previousPeriod.isPresent()) {
            return previousPeriod.get().getRemainingValue();
        }

        // If no previous period found, start with purchase price (first month)
        return asset.getPurchasePrice();
    }

    /**
     * Validate period parameters
     *
     * @param month Tháng (1-12)
     * @param year Năm (>= 2000)
     * @throws IllegalArgumentException nếu tham số không hợp lệ
     */
    private void validatePeriodParameters(Integer month, Integer year) {
        if (month == null || month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12, got: " + month);
        }

        if (year == null || year < 2000) {
            throw new IllegalArgumentException("Year must be >= 2000, got: " + year);
        }
    }

    /**
     * Manual Entity → DTO mapping
     * Senior Spring Boot practice: không dùng Lombok, ModelMapper
     *
     * @param entity DepreciationHistory entity
     * @return DepreciationHistoryDTO
     */
    private DepreciationHistoryDTO mapEntityToDTO(DepreciationHistory entity) {
        if (entity == null) {
            return null;
        }

        DepreciationHistoryDTO dto = new DepreciationHistoryDTO();
        dto.setId(entity.getId());
        dto.setAssetId(entity.getAsset().getId());
        dto.setAssetCode(entity.getAsset().getAssetCode());
        dto.setAssetName(entity.getAsset().getName());
        dto.setPeriodMonth(entity.getPeriodMonth());
        dto.setPeriodYear(entity.getPeriodYear());
        dto.setDepreciationAmount(entity.getDepreciationAmount());
        dto.setRemainingValue(entity.getRemainingValue());
        dto.setPurchasePrice(entity.getAsset().getPurchasePrice());
        dto.setNotes(entity.getNotes());
        dto.setCalculatedAt(entity.getCalculatedAt());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
