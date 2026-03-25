package com.nhom18.quanlytaisan.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO cho Depreciation History (Lịch sử khấu hao)
 */
public class DepreciationHistoryDTO {

    private Long id;
    private Long assetId;
    private String assetName;
    private Integer periodMonth;
    private Integer periodYear;
    private BigDecimal depreciationAmount;
    private BigDecimal remainingValue;
    private String notes;
    private LocalDateTime calculatedAt;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public DepreciationHistoryDTO() {
    }

    /**
     * Constructor đầy đủ tham số
     */
    public DepreciationHistoryDTO(Long id, Long assetId, String assetName,
                                  Integer periodMonth, Integer periodYear,
                                  BigDecimal depreciationAmount, BigDecimal remainingValue,
                                  String notes, LocalDateTime calculatedAt) {
        this.id = id;
        this.assetId = assetId;
        this.assetName = assetName;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.depreciationAmount = depreciationAmount;
        this.remainingValue = remainingValue;
        this.notes = notes;
        this.calculatedAt = calculatedAt;
    }

    /**
     * Constructor không có ID (dùng khi tạo mới)
     */
    public DepreciationHistoryDTO(Long assetId, String assetName,
                                  Integer periodMonth, Integer periodYear,
                                  BigDecimal depreciationAmount, BigDecimal remainingValue,
                                  String notes) {
        this.assetId = assetId;
        this.assetName = assetName;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.depreciationAmount = depreciationAmount;
        this.remainingValue = remainingValue;
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

    @Override
    public String toString() {
        return "DepreciationHistoryDTO{" +
                "id=" + id +
                ", assetId=" + assetId +
                ", assetName='" + assetName + '\'' +
                ", periodMonth=" + periodMonth +
                ", periodYear=" + periodYear +
                ", depreciationAmount=" + depreciationAmount +
                ", remainingValue=" + remainingValue +
                ", calculatedAt=" + calculatedAt +
                '}';
    }
}
