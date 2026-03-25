package com.nhom18.quanlytaisan.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity AssetTransfer - Lưu trữ lịch sử bàn giao/điều chuyển tài sản
 */
@Entity
@Table(name = "asset_transfers")
public class AssetTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false, foreignKey = @ForeignKey(name = "fk_asset_transfers_asset"))
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_department_id", foreignKey = @ForeignKey(name = "fk_asset_transfers_from_dept"))
    private Department fromDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_department_id", nullable = false, foreignKey = @ForeignKey(name = "fk_asset_transfers_to_dept"))
    private Department toDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_employee_id", foreignKey = @ForeignKey(name = "fk_asset_transfers_from_emp"))
    private Employee fromEmployee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_employee_id", foreignKey = @ForeignKey(name = "fk_asset_transfers_to_emp"))
    private Employee toEmployee;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "transfer_by", length = 255)
    private String transferBy;

@Column(name = "created_at", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @org.hibernate.annotations.UpdateTimestamp
    private LocalDateTime updatedAt;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public AssetTransfer() {
    }

    /**
     * Constructor đầy đủ tham số
     */
    public AssetTransfer(Long id, Asset asset, Department fromDepartment, Department toDepartment,
                          Employee fromEmployee, Employee toEmployee, LocalDate transferDate,
                          String reason, String transferBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.asset = asset;
        this.fromDepartment = fromDepartment;
        this.toDepartment = toDepartment;
        this.fromEmployee = fromEmployee;
        this.toEmployee = toEmployee;
        this.transferDate = transferDate;
        this.reason = reason;
        this.transferBy = transferBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Constructor không có ID (dùng khi INSERT)
     */
    public AssetTransfer(Asset asset, Department fromDepartment, Department toDepartment,
                          Employee fromEmployee, Employee toEmployee, LocalDate transferDate,
                          String reason, String transferBy) {
        this.asset = asset;
        this.fromDepartment = fromDepartment;
        this.toDepartment = toDepartment;
        this.fromEmployee = fromEmployee;
        this.toEmployee = toEmployee;
        this.transferDate = transferDate;
        this.reason = reason;
        this.transferBy = transferBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public Department getFromDepartment() {
        return fromDepartment;
    }

    public void setFromDepartment(Department fromDepartment) {
        this.fromDepartment = fromDepartment;
    }

    public Department getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(Department toDepartment) {
        this.toDepartment = toDepartment;
    }

    public Employee getFromEmployee() {
        return fromEmployee;
    }

    public void setFromEmployee(Employee fromEmployee) {
        this.fromEmployee = fromEmployee;
    }

    public Employee getToEmployee() {
        return toEmployee;
    }

    public void setToEmployee(Employee toEmployee) {
        this.toEmployee = toEmployee;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTransferBy() {
        return transferBy;
    }

    public void setTransferBy(String transferBy) {
        this.transferBy = transferBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AssetTransfer{" +
                "id=" + id +
                ", assetId=" + (asset != null ? asset.getId() : null) +
                ", fromDepartmentId=" + (fromDepartment != null ? fromDepartment.getId() : null) +
                ", toDepartmentId=" + (toDepartment != null ? toDepartment.getId() : null) +
                ", fromEmployeeId=" + (fromEmployee != null ? fromEmployee.getId() : null) +
                ", toEmployeeId=" + (toEmployee != null ? toEmployee.getId() : null) +
                ", transferDate=" + transferDate +
                ", reason='" + reason + '\'' +
                ", transferBy='" + transferBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
