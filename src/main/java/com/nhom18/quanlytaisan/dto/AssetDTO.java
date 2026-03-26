package com.nhom18.quanlytaisan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AssetDTO {

    private Long id;
    private String assetCode;
    private String qrCodeText;
    private String name;
    private BigDecimal purchasePrice;
    private BigDecimal currentValue;
    private LocalDate purchaseDate;
    private LocalDate usageStartDate;
    private String status;
    private String warrantyProvider;
    private LocalDate warrantyExpiryDate;
    private Integer usefulLifeMonths = 36; // Tuổi thọ hữu ích (tháng), mặc định 36
    private Integer maintenanceCycle;
    private LocalDate lastMaintenanceDate;
    private LocalDate nextMaintenanceDate;

    // Foreign Key Fields
    private Long categoryId;
    private String categoryName;
    private Long currentDepartmentId;
    private String currentDepartmentName;
    private Long currentEmployeeId;
    private String currentEmployeeName;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public AssetDTO() {
    }

    /**
     * Constructor có tham số đầy đủ
     */
    public AssetDTO(Long id, String assetCode, String qrCodeText, String name,
                    BigDecimal purchasePrice, BigDecimal currentValue, LocalDate purchaseDate,
                    LocalDate usageStartDate, String status, String warrantyProvider,
                    LocalDate warrantyExpiryDate, Long categoryId, String categoryName,
                    Long currentDepartmentId, String currentDepartmentName,
                    Long currentEmployeeId, String currentEmployeeName) {
        this.id = id;
        this.assetCode = assetCode;
        this.qrCodeText = qrCodeText;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.currentValue = currentValue;
        this.purchaseDate = purchaseDate;
        this.usageStartDate = usageStartDate;
        this.status = status;
        this.warrantyProvider = warrantyProvider;
        this.warrantyExpiryDate = warrantyExpiryDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.currentDepartmentId = currentDepartmentId;
        this.currentDepartmentName = currentDepartmentName;
        this.currentEmployeeId = currentEmployeeId;
        this.currentEmployeeName = currentEmployeeName;
    }

    /**
     * Constructor không có ID (dùng khi tạo mới)
     */
    public AssetDTO(String assetCode, String qrCodeText, String name,
                    BigDecimal purchasePrice, BigDecimal currentValue, LocalDate purchaseDate,
                    LocalDate usageStartDate, String status, String warrantyProvider,
                    LocalDate warrantyExpiryDate, Long categoryId, String categoryName,
                    Long currentDepartmentId, String currentDepartmentName,
                    Long currentEmployeeId, String currentEmployeeName) {
        this.assetCode = assetCode;
        this.qrCodeText = qrCodeText;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.currentValue = currentValue;
        this.purchaseDate = purchaseDate;
        this.usageStartDate = usageStartDate;
        this.status = status;
        this.warrantyProvider = warrantyProvider;
        this.warrantyExpiryDate = warrantyExpiryDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.currentDepartmentId = currentDepartmentId;
        this.currentDepartmentName = currentDepartmentName;
        this.currentEmployeeId = currentEmployeeId;
        this.currentEmployeeName = currentEmployeeName;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCurrentDepartmentId() {
        return currentDepartmentId;
    }

    public void setCurrentDepartmentId(Long currentDepartmentId) {
        this.currentDepartmentId = currentDepartmentId;
    }

    public String getCurrentDepartmentName() {
        return currentDepartmentName;
    }

    public void setCurrentDepartmentName(String currentDepartmentName) {
        this.currentDepartmentName = currentDepartmentName;
    }

    public Long getCurrentEmployeeId() {
        return currentEmployeeId;
    }

    public void setCurrentEmployeeId(Long currentEmployeeId) {
        this.currentEmployeeId = currentEmployeeId;
    }

    public String getCurrentEmployeeName() {
        return currentEmployeeName;
    }

    public void setCurrentEmployeeName(String currentEmployeeName) {
        this.currentEmployeeName = currentEmployeeName;
    }

    public Integer getUsefulLifeMonths() {
        return usefulLifeMonths != null ? usefulLifeMonths : 36;
    }

    public void setUsefulLifeMonths(Integer usefulLifeMonths) {
        this.usefulLifeMonths = usefulLifeMonths != null ? usefulLifeMonths : 36;
    }

    public Integer getMaintenanceCycle() {
        return maintenanceCycle;
    }

    public void setMaintenanceCycle(Integer maintenanceCycle) {
        this.maintenanceCycle = maintenanceCycle;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    @Override
    public String toString() {
        return "AssetDTO{" +
                "id=" + id +
                ", assetCode='" + assetCode + '\'' +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", status='" + status + '\'' +
                ", currentDepartmentId=" + currentDepartmentId +
                ", currentDepartmentName='" + currentDepartmentName + '\'' +
                '}';
    }
}


