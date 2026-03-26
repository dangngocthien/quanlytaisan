package com.nhom18.quanlytaisan.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WarrantyRecordDTO {

    private Long id;
    private Long assetId;
    private String assetName;
    private String assetCode;
    
    private String providerCompany;
    private String contactPhone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
    
    private String attachmentFileName;
    private String attachmentType;
    
    private String fileDownloadUrl; 
    
    private LocalDateTime createdAt;
    
    public WarrantyRecordDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public String getAssetCode() { return assetCode; }
    public void setAssetCode(String assetCode) { this.assetCode = assetCode; }

    public String getProviderCompany() { return providerCompany; }
    public void setProviderCompany(String providerCompany) { this.providerCompany = providerCompany; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getAttachmentFileName() { return attachmentFileName; }
    public void setAttachmentFileName(String attachmentFileName) { this.attachmentFileName = attachmentFileName; }

    public String getAttachmentType() { return attachmentType; }
    public void setAttachmentType(String attachmentType) { this.attachmentType = attachmentType; }

    public String getFileDownloadUrl() { return fileDownloadUrl; }
    public void setFileDownloadUrl(String fileDownloadUrl) { this.fileDownloadUrl = fileDownloadUrl; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
