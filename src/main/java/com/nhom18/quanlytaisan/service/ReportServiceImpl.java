package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetDTO;
import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation cho quản lý báo cáo
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private AssetRepository assetRepository;

    /**
     * Lấy danh sách tài sản theo phòng ban
     */
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

    /**
     * Lấy danh sách tài sản theo danh mục
     */
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

    /**
     * Lấy danh sách tài sản theo trạng thái
     */
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

    /**
     * Lấy danh sách tất cả tài sản
     */
    @Override
    public List<AssetDTO> getAllAssets() {
        return assetRepository
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy tổng giá trị tài sản theo phòng ban
     */
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

    /**
     * Lấy tổng giá trị tài sản theo danh mục
     */
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

    /**
     * Helper method: Convert Entity sang DTO
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
