package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetTransferDTO;
import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.entity.AssetTransfer;
import com.nhom18.quanlytaisan.entity.Department;
import com.nhom18.quanlytaisan.entity.Employee;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import com.nhom18.quanlytaisan.repository.AssetTransferRepository;
import com.nhom18.quanlytaisan.repository.DepartmentRepository;
import com.nhom18.quanlytaisan.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation cho quản lý bàn giao/điều chuyển tài sản
 */
@Service
public class AssetTransferServiceImpl implements AssetTransferService {

    @Autowired
    private AssetTransferRepository assetTransferRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Lấy lịch sử bàn giao/điều chuyển của một tài sản theo Asset ID
     */
    @Override
    public List<AssetTransferDTO> getHistoryByAssetId(Long assetId) {
        return assetTransferRepository
                .findByAsset_Id(assetId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Thực hiện bàn giao/điều chuyển tài sản
     * Là TRANSACTIONAL: thực hiện 2 bước trong 1 transaction
     * Bước 1: Lưu thông tin bàn giao vào bảng AssetTransfer
     * Bước 2: Cập nhật currentDepartmentId và currentEmployeeId của tài sản
     */
    @Override
    @Transactional
    public AssetTransferDTO transferAsset(AssetTransferDTO dto) {
        // 1. Lấy entity từ ID (hoặc null nếu không tìm được)
        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new RuntimeException("Tài sản không tồn tại với ID: " + dto.getAssetId()));

        // Kiểm tra ngày điều chuyển phải sau hoặc bằng ngày sử dụng của tài sản
        if (dto.getTransferDate() != null && asset.getUsageStartDate() != null) {
            if (dto.getTransferDate().isBefore(asset.getUsageStartDate())) {
                throw new IllegalArgumentException("Ngày điều chuyển không được trước ngày sử dụng của tài sản (ngày " + asset.getUsageStartDate() + ").");
            }
        }

        Department fromDept = dto.getFromDepartmentId() != null
                ? departmentRepository.findById(dto.getFromDepartmentId()).orElse(null)
                : null;

        Department toDept = departmentRepository.findById(dto.getToDepartmentId())
                .orElseThrow(() -> new RuntimeException("Phòng ban đích không tồn tại với ID: " + dto.getToDepartmentId()));

        Employee fromEmp = dto.getFromEmployeeId() != null
                ? employeeRepository.findById(dto.getFromEmployeeId()).orElse(null)
                : null;

        Employee toEmp = dto.getToEmployeeId() != null
                ? employeeRepository.findById(dto.getToEmployeeId()).orElse(null)
                : null;

        // 2. Tạo entity AssetTransfer mới
        AssetTransfer assetTransfer = new AssetTransfer();
        assetTransfer.setAsset(asset);
        assetTransfer.setFromDepartment(fromDept);
        assetTransfer.setToDepartment(toDept);
        assetTransfer.setFromEmployee(fromEmp);
        assetTransfer.setToEmployee(toEmp);
        assetTransfer.setTransferDate(dto.getTransferDate());
        assetTransfer.setReason(dto.getReason());
        assetTransfer.setTransferBy(dto.getTransferBy());
        assetTransfer.setCreatedAt(LocalDateTime.now());
        assetTransfer.setUpdatedAt(LocalDateTime.now());

        // 3. LƯU VÀO DATABASE (Bước 1)
        AssetTransfer savedTransfer = assetTransferRepository.save(assetTransfer);

        // 4. CẬP NHẬT TÀI SẢN VỚI VỊ TRÍ MỚI (Bước 2)
        asset.setCurrentDepartment(toDept);
        asset.setCurrentEmployee(toEmp);
        asset.setUpdatedAt(LocalDateTime.now());
        assetRepository.save(asset);

        // 5. Trả về DTO
        return convertToDTO(savedTransfer);
    }

    /**
     * Lấy tất cả lịch sử bàn giao
     */
    @Override
    public List<AssetTransferDTO> getAllTransferHistory() {
        return assetTransferRepository
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy chi tiết một bản ghi bàn giao theo ID
     */
    @Override
    public AssetTransferDTO getTransferById(Long id) {
        return assetTransferRepository
                .findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Bản ghi bàn giao không tồn tại với ID: " + id));
    }

    /**
     * Helper method: Convert Entity sang DTO
     */
    private AssetTransferDTO convertToDTO(AssetTransfer entity) {
        AssetTransferDTO dto = new AssetTransferDTO();
        dto.setId(entity.getId());
        
        // Asset Info
        if (entity.getAsset() != null) {
            dto.setAssetId(entity.getAsset().getId());
            dto.setAssetName(entity.getAsset().getName());
        }
        
        // From Department Info
        if (entity.getFromDepartment() != null) {
            dto.setFromDepartmentId(entity.getFromDepartment().getId());
            dto.setFromDepartmentName(entity.getFromDepartment().getName());
        }
        
        // To Department Info
        if (entity.getToDepartment() != null) {
            dto.setToDepartmentId(entity.getToDepartment().getId());
            dto.setToDepartmentName(entity.getToDepartment().getName());
        }
        
        // From Employee Info
        if (entity.getFromEmployee() != null) {
            dto.setFromEmployeeId(entity.getFromEmployee().getId());
            dto.setFromEmployeeName(entity.getFromEmployee().getFullName());
        }
        
        // To Employee Info
        if (entity.getToEmployee() != null) {
            dto.setToEmployeeId(entity.getToEmployee().getId());
            dto.setToEmployeeName(entity.getToEmployee().getFullName());
        }
        
        dto.setTransferDate(entity.getTransferDate());
        dto.setReason(entity.getReason());
        dto.setTransferBy(entity.getTransferBy());
        
        return dto;
    }
}
