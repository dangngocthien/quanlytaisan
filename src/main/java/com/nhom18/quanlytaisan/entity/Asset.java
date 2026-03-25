package com.nhom18.quanlytaisan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_code", nullable = false, unique = true, length = 100)
    private String assetCode;

    @Column(name = "qr_code_text", unique = true, length = 255)
    private String qrCodeText;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_assets_category"))
    private AssetCategory category;

    @Column(name = "purchase_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "current_value", nullable = false, precision = 15, scale = 2)
    private BigDecimal currentValue;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "usage_start_date")
    private LocalDate usageStartDate;

    @Column(name = "status", length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_department_id", foreignKey = @ForeignKey(name = "fk_assets_department"))
    private Department currentDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_employee_id", foreignKey = @ForeignKey(name = "fk_assets_employee"))
    private Employee currentEmployee;

    @Column(name = "warranty_provider", length = 255)
    private String warrantyProvider;

    @Column(name = "warranty_expiry_date")
    private LocalDate warrantyExpiryDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public Asset() {
    }

    /**
     * Constructor đầy đủ tham số
     */
    public Asset(Long id, String assetCode, String qrCodeText, String name, AssetCategory category,
                 BigDecimal purchasePrice, BigDecimal currentValue, LocalDate purchaseDate,
                 LocalDate usageStartDate, String status, Department currentDepartment,
                 Employee currentEmployee, String warrantyProvider, LocalDate warrantyExpiryDate,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.assetCode = assetCode;
        this.qrCodeText = qrCodeText;
        this.name = name;
        this.category = category;
        this.purchasePrice = purchasePrice;
        this.currentValue = currentValue;
        this.purchaseDate = purchaseDate;
        this.usageStartDate = usageStartDate;
        this.status = status;
        this.currentDepartment = currentDepartment;
        this.currentEmployee = currentEmployee;
        this.warrantyProvider = warrantyProvider;
        this.warrantyExpiryDate = warrantyExpiryDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Constructor không có ID (dùng khi INSERT)
     */
    public Asset(String assetCode, String qrCodeText, String name, AssetCategory category,
                 BigDecimal purchasePrice, BigDecimal currentValue, LocalDate purchaseDate,
                 LocalDate usageStartDate, String status, Department currentDepartment,
                 Employee currentEmployee, String warrantyProvider, LocalDate warrantyExpiryDate) {
        this.assetCode = assetCode;
        this.qrCodeText = qrCodeText;
        this.name = name;
        this.category = category;
        this.purchasePrice = purchasePrice;
        this.currentValue = currentValue;
        this.purchaseDate = purchaseDate;
        this.usageStartDate = usageStartDate;
        this.status = status;
        this.currentDepartment = currentDepartment;
        this.currentEmployee = currentEmployee;
        this.warrantyProvider = warrantyProvider;
        this.warrantyExpiryDate = warrantyExpiryDate;
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

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getQrCodeText() {
        return qrCodeText;
    }

    public void setQrCodeText(String qrCodeText) {
        this.qrCodeText = qrCodeText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssetCategory getCategory() {
        return category;
    }

    public void setCategory(AssetCategory category) {
        this.category = category;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getUsageStartDate() {
        return usageStartDate;
    }

    public void setUsageStartDate(LocalDate usageStartDate) {
        this.usageStartDate = usageStartDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Department getCurrentDepartment() {
        return currentDepartment;
    }

    public void setCurrentDepartment(Department currentDepartment) {
        this.currentDepartment = currentDepartment;
    }

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    public String getWarrantyProvider() {
        return warrantyProvider;
    }

    public void setWarrantyProvider(String warrantyProvider) {
        this.warrantyProvider = warrantyProvider;
    }

    public LocalDate getWarrantyExpiryDate() {
        return warrantyExpiryDate;
    }

    public void setWarrantyExpiryDate(LocalDate warrantyExpiryDate) {
        this.warrantyExpiryDate = warrantyExpiryDate;
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
        return "Asset{" +
                "id=" + id +
                ", assetCode='" + assetCode + '\'' +
                ", name='" + name + '\'' +
                ", category=" + (category != null ? category.getId() : null) +
                ", purchasePrice=" + purchasePrice +
                ", currentValue=" + currentValue +
                ", status='" + status + '\'' +
                ", currentDepartment=" + (currentDepartment != null ? currentDepartment.getId() : null) +
                ", currentEmployee=" + (currentEmployee != null ? currentEmployee.getId() : null) +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
