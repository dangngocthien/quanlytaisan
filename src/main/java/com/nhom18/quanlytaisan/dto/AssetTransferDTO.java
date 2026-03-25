package com.nhom18.quanlytaisan.dto;

import java.time.LocalDate;

/**
 * DTO cho Asset Transfer (Bàn giao/Điều chuyển tài sản)
 */
public class AssetTransferDTO {

    private Long id;
    private Long assetId;
    private String assetName;
    private Long fromDepartmentId;
    private String fromDepartmentName;
    private Long toDepartmentId;
    private String toDepartmentName;
    private Long fromEmployeeId;
    private String fromEmployeeName;
    private Long toEmployeeId;
    private String toEmployeeName;
    private LocalDate transferDate;
    private String reason;
    private String transferBy;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public AssetTransferDTO() {
    }

    /**
     * Constructor đầy đủ tham số
     */
    public AssetTransferDTO(Long id, Long assetId, String assetName,
                            Long fromDepartmentId, String fromDepartmentName,
                            Long toDepartmentId, String toDepartmentName,
                            Long fromEmployeeId, String fromEmployeeName,
                            Long toEmployeeId, String toEmployeeName,
                            LocalDate transferDate, String reason, String transferBy) {
        this.id = id;
        this.assetId = assetId;
        this.assetName = assetName;
        this.fromDepartmentId = fromDepartmentId;
        this.fromDepartmentName = fromDepartmentName;
        this.toDepartmentId = toDepartmentId;
        this.toDepartmentName = toDepartmentName;
        this.fromEmployeeId = fromEmployeeId;
        this.fromEmployeeName = fromEmployeeName;
        this.toEmployeeId = toEmployeeId;
        this.toEmployeeName = toEmployeeName;
        this.transferDate = transferDate;
        this.reason = reason;
        this.transferBy = transferBy;
    }

    /**
     * Constructor không có ID (dùng khi tạo mới)
     */
    public AssetTransferDTO(Long assetId, String assetName,
                            Long fromDepartmentId, String fromDepartmentName,
                            Long toDepartmentId, String toDepartmentName,
                            Long fromEmployeeId, String fromEmployeeName,
                            Long toEmployeeId, String toEmployeeName,
                            LocalDate transferDate, String reason, String transferBy) {
        this.assetId = assetId;
        this.assetName = assetName;
        this.fromDepartmentId = fromDepartmentId;
        this.fromDepartmentName = fromDepartmentName;
        this.toDepartmentId = toDepartmentId;
        this.toDepartmentName = toDepartmentName;
        this.fromEmployeeId = fromEmployeeId;
        this.fromEmployeeName = fromEmployeeName;
        this.toEmployeeId = toEmployeeId;
        this.toEmployeeName = toEmployeeName;
        this.transferDate = transferDate;
        this.reason = reason;
        this.transferBy = transferBy;
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

    public Long getFromDepartmentId() {
        return fromDepartmentId;
    }

    public void setFromDepartmentId(Long fromDepartmentId) {
        this.fromDepartmentId = fromDepartmentId;
    }

    public String getFromDepartmentName() {
        return fromDepartmentName;
    }

    public void setFromDepartmentName(String fromDepartmentName) {
        this.fromDepartmentName = fromDepartmentName;
    }

    public Long getToDepartmentId() {
        return toDepartmentId;
    }

    public void setToDepartmentId(Long toDepartmentId) {
        this.toDepartmentId = toDepartmentId;
    }

    public String getToDepartmentName() {
        return toDepartmentName;
    }

    public void setToDepartmentName(String toDepartmentName) {
        this.toDepartmentName = toDepartmentName;
    }

    public Long getFromEmployeeId() {
        return fromEmployeeId;
    }

    public void setFromEmployeeId(Long fromEmployeeId) {
        this.fromEmployeeId = fromEmployeeId;
    }

    public String getFromEmployeeName() {
        return fromEmployeeName;
    }

    public void setFromEmployeeName(String fromEmployeeName) {
        this.fromEmployeeName = fromEmployeeName;
    }

    public Long getToEmployeeId() {
        return toEmployeeId;
    }

    public void setToEmployeeId(Long toEmployeeId) {
        this.toEmployeeId = toEmployeeId;
    }

    public String getToEmployeeName() {
        return toEmployeeName;
    }

    public void setToEmployeeName(String toEmployeeName) {
        this.toEmployeeName = toEmployeeName;
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

    @Override
    public String toString() {
        return "AssetTransferDTO{" +
                "id=" + id +
                ", assetId=" + assetId +
                ", assetName='" + assetName + '\'' +
                ", fromDepartmentId=" + fromDepartmentId +
                ", fromDepartmentName='" + fromDepartmentName + '\'' +
                ", toDepartmentId=" + toDepartmentId +
                ", toDepartmentName='" + toDepartmentName + '\'' +
                ", fromEmployeeId=" + fromEmployeeId +
                ", fromEmployeeName='" + fromEmployeeName + '\'' +
                ", toEmployeeId=" + toEmployeeId +
                ", toEmployeeName='" + toEmployeeName + '\'' +
                ", transferDate=" + transferDate +
                ", reason='" + reason + '\'' +
                ", transferBy='" + transferBy + '\'' +
                '}';
    }
}
