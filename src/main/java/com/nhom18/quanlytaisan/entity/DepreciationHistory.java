package com.nhom18.quanlytaisan.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity DepreciationHistory - Lưu trữ lịch sử khấu hao hàng tháng
 */
@Entity
@Table(name = "depreciation_histories")
public class DepreciationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false, foreignKey = @ForeignKey(name = "fk_depreciation_histories_asset"))
    private Asset asset;

    @Column(name = "period_month", nullable = false)
    private Integer periodMonth;

    @Column(name = "period_year", nullable = false)
    private Integer periodYear;

    @Column(name = "depreciation_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "remaining_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingValue;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;

    // ============ CONSTRUCTORS ============
    
    /**
     * Constructor không tham số
     */
    public DepreciationHistory() {
    }

    /**
     * Constructor đầy đủ tham số
     */
    public DepreciationHistory(Long id, Asset asset, Integer periodMonth, Integer periodYear,
                               BigDecimal depreciationAmount, BigDecimal remainingValue,
                               String notes, LocalDateTime calculatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.asset = asset;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.depreciationAmount = depreciationAmount;
        this.remainingValue = remainingValue;
        this.notes = notes;
        this.calculatedAt = calculatedAt;
        this.createdAt = createdAt;
    }

    /**
     * Constructor không có ID (dùng khi INSERT)
     */
    public DepreciationHistory(Asset asset, Integer periodMonth, Integer periodYear,
                               BigDecimal depreciationAmount, BigDecimal remainingValue,
                               String notes) {
        this.asset = asset;
        this.periodMonth = periodMonth;
        this.periodYear = periodYear;
        this.depreciationAmount = depreciationAmount;
        this.remainingValue = remainingValue;
        this.notes = notes;
        this.calculatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // ============ GETTERS & SETTERS ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "DepreciationHistory{" +
                "id=" + id +
                ", assetId=" + (asset != null ? asset.getId() : null) +
                ", periodMonth=" + periodMonth +
                ", periodYear=" + periodYear +
                ", depreciationAmount=" + depreciationAmount +
                ", remainingValue=" + remainingValue +
                ", calculatedAt=" + calculatedAt +
                '}';
    }
}
