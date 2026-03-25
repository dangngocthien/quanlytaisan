package com.nhom18.quanlytaisan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO cho Depreciation History (Lịch sử khấu hao)
 */
public class DepreciationHistoryDTO {

    private Long id;
    private Long assetId;
    private String assetCode;
    private String assetName;
    private Integer periodMonth;
    private Integer periodYear;
    private BigDecimal depreciationAmount;
    private BigDecimal remainingValue;
    private BigDecimal purchasePrice;
    private String notes;
    private LocalDateTime calculatedAt;
    private LocalDateTime createdAt;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public DepreciationHistoryDTO() {
    }

    /**
     * Constructor đầy đủ tham số (sử dụng trong mapping từ Entity)
     */
    public DepreciationHistoryDTO(Long id, Long assetId, String assetCode, String assetName,
                                  Integer periodMonth, Integer periodYear,
                                  BigDecimal depreciationAmount, BigDecimal remainingValue,
                                  BigDecimal purchasePrice, String notes,
                                  LocalDateTime calculatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.assetId = assetId;
        this.assetCode = assetCode;
        this.assetName = assetName;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.depreciationAmount = depreciationAmount;
        this.remainingValue = remainingValue;
        this.purchasePrice = purchasePrice;
        this.notes = notes;
        this.calculatedAt = calculatedAt;
        this.createdAt = createdAt;
    }

    /**
     * Constructor không có ID (dùng khi tạo mới)
     */
    public DepreciationHistoryDTO(Long assetId, String assetCode, String assetName,
                                  Integer periodMonth, Integer periodYear,
                                  BigDecimal depreciationAmount, BigDecimal remainingValue,
                                  BigDecimal purchasePrice, String notes) {
        this.assetId = assetId;
        this.assetCode = assetCode;
        this.assetName = assetName;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.depreciationAmount = depreciationAmount;
        this.remainingValue = remainingValue;
        this.purchasePrice = purchasePrice;
        this.notes = notes;
    }

    // ============ GETTERS & SETTERS ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssetId() {
        return assetId;
    }

    public void setAssetId(Long assetId) {
        this.assetId = assetId;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public Integer getPeriodYear() {
        return periodYear;
    }

    public void setPeriodYear(Integer periodYear) {
        this.periodYear = periodYear;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getRemainingValue() {
        return remainingValue;
    }

    public void setRemainingValue(BigDecimal remainingValue) {
        this.remainingValue = remainingValue;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "DepreciationHistoryDTO{" +
                "id=" + id +
                ", assetId=" + assetId +
                ", assetCode='" + assetCode + '\'' +
                ", assetName='" + assetName + '\'' +
                ", periodMonth=" + periodMonth +
                ", periodYear=" + periodYear +
                ", depreciationAmount=" + depreciationAmount +
                ", remainingValue=" + remainingValue +
                ", purchasePrice=" + purchasePrice +
                ", calculatedAt=" + calculatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
