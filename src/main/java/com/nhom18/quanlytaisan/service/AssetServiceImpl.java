package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetDTO;
import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.entity.AssetCategory;
import com.nhom18.quanlytaisan.entity.Department;
import com.nhom18.quanlytaisan.entity.Employee;
import com.nhom18.quanlytaisan.repository.AssetCategoryRepository;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import com.nhom18.quanlytaisan.repository.DepartmentRepository;
import com.nhom18.quanlytaisan.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetCategoryRepository assetCategoryRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Constructor Dependency Injection
     */
    public AssetServiceImpl(AssetRepository assetRepository,
                          AssetCategoryRepository assetCategoryRepository,
                          DepartmentRepository departmentRepository,
                          EmployeeRepository employeeRepository) {
        this.assetRepository = assetRepository;
        this.assetCategoryRepository = assetCategoryRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<AssetDTO> getAll() {
        List<Asset> assets = assetRepository.findAll();
        return assets.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AssetDTO getById(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));
        return mapEntityToDTO(asset);
    }

    @Override
    public AssetDTO create(AssetDTO assetDTO) {
        // Kiểm tra asset code đã tồn tại chưa
        if (assetRepository.existsByAssetCode(assetDTO.getAssetCode())) {
            throw new RuntimeException("Asset code already exists: " + assetDTO.getAssetCode());
        }

        // Mapping DTO → Entity
        Asset asset = mapDTOToEntity(assetDTO);

        // Lưu vào database
        Asset savedAsset = assetRepository.save(asset);

        // Mapping Entity → DTO để trả về
        return mapEntityToDTO(savedAsset);
    }

    @Override
    public AssetDTO update(Long id, AssetDTO assetDTO) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        // Cập nhật dữ liệu cơ bản
        asset.setName(assetDTO.getName());
        asset.setPurchasePrice(assetDTO.getPurchasePrice());
        asset.setCurrentValue(assetDTO.getCurrentValue());
        asset.setPurchaseDate(assetDTO.getPurchaseDate());
        asset.setUsageStartDate(assetDTO.getUsageStartDate());
        asset.setStatus(assetDTO.getStatus());
        asset.setWarrantyProvider(assetDTO.getWarrantyProvider());
        asset.setWarrantyExpiryDate(assetDTO.getWarrantyExpiryDate());

        // Nếu asset code thay đổi, kiểm tra xem code mới đã tồn tại không
        if (!asset.getAssetCode().equals(assetDTO.getAssetCode())) {
            if (assetRepository.existsByAssetCode(assetDTO.getAssetCode())) {
                throw new RuntimeException("Asset code already exists: " + assetDTO.getAssetCode());
            }
            asset.setAssetCode(assetDTO.getAssetCode());
        }

        // Cập nhật category nếu categoryId thay đổi
        if (assetDTO.getCategoryId() != null) {
            AssetCategory category = assetCategoryRepository.findById(assetDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("AssetCategory not found with id: " + assetDTO.getCategoryId()));
            asset.setCategory(category);
        }

        // Cập nhật phòng ban nếu currentDepartmentId thay đổi
        if (assetDTO.getCurrentDepartmentId() != null) {
            Department department = departmentRepository.findById(assetDTO.getCurrentDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + assetDTO.getCurrentDepartmentId()));
            asset.setCurrentDepartment(department);
        }

        // Cập nhật nhân viên nếu currentEmployeeId thay đổi
        if (assetDTO.getCurrentEmployeeId() != null) {
            Employee employee = employeeRepository.findById(assetDTO.getCurrentEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + assetDTO.getCurrentEmployeeId()));
            asset.setCurrentEmployee(employee);
        }

        // Lưu cập nhật
        Asset updatedAsset = assetRepository.save(asset);

        // Trả về DTO
        return mapEntityToDTO(updatedAsset);
    }

    @Override
    public void delete(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        assetRepository.delete(asset);
    }

    @Override
    public AssetDTO findByAssetCode(String assetCode) {
        Asset asset = assetRepository.findByAssetCode(assetCode);
        if (asset == null) {
            throw new RuntimeException("Asset not found with code: " + assetCode);
        }
        return mapEntityToDTO(asset);
    }

    @Override
    public List<AssetDTO> findByCategoryId(Long categoryId) {
        List<Asset> assets = assetRepository.findByCategoryId(categoryId);
        return assets.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetDTO> findByCurrentDepartmentId(Long departmentId) {
        List<Asset> assets = assetRepository.findByCurrentDepartmentId(departmentId);
        return assets.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssetDTO> findByStatus(String status) {
        List<Asset> assets = assetRepository.findByStatus(status);
        return assets.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mapping Entity → DTO (thủ công) - Includes Category, Department, Employee info
     */
    private AssetDTO mapEntityToDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setAssetCode(asset.getAssetCode());
        dto.setQrCodeText(asset.getQrCodeText());
        dto.setName(asset.getName());
        dto.setPurchasePrice(asset.getPurchasePrice());
        dto.setCurrentValue(asset.getCurrentValue());
        dto.setPurchaseDate(asset.getPurchaseDate());
        dto.setUsageStartDate(asset.getUsageStartDate());
        dto.setStatus(asset.getStatus());
        dto.setWarrantyProvider(asset.getWarrantyProvider());
        dto.setWarrantyExpiryDate(asset.getWarrantyExpiryDate());

        // Map Category Info
        if (asset.getCategory() != null) {
            dto.setCategoryId(asset.getCategory().getId());
            dto.setCategoryName(asset.getCategory().getName());
        }

        // Map Department Info
        if (asset.getCurrentDepartment() != null) {
            dto.setCurrentDepartmentId(asset.getCurrentDepartment().getId());
            dto.setCurrentDepartmentName(asset.getCurrentDepartment().getName());
        }

        // Map Employee Info
        if (asset.getCurrentEmployee() != null) {
            dto.setCurrentEmployeeId(asset.getCurrentEmployee().getId());
            dto.setCurrentEmployeeName(asset.getCurrentEmployee().getFullName());
        }

        return dto;
    }

    /**
     * Mapping DTO → Entity (thủ công)
     */
    private Asset mapDTOToEntity(AssetDTO dto) {
        Asset asset = new Asset();
        asset.setAssetCode(dto.getAssetCode());
        asset.setQrCodeText(dto.getQrCodeText());
        asset.setName(dto.getName());
        asset.setPurchasePrice(dto.getPurchasePrice());
        asset.setCurrentValue(dto.getCurrentValue());
        asset.setPurchaseDate(dto.getPurchaseDate());
        asset.setUsageStartDate(dto.getUsageStartDate());
        asset.setStatus(dto.getStatus());
        asset.setWarrantyProvider(dto.getWarrantyProvider());
        asset.setWarrantyExpiryDate(dto.getWarrantyExpiryDate());
        asset.setCreatedAt(java.time.LocalDateTime.now());

        // Set Category nếu có categoryId
        if (dto.getCategoryId() != null) {
            AssetCategory category = assetCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("AssetCategory not found with id: " + dto.getCategoryId()));
            asset.setCategory(category);
        }

        // Set Department nếu có currentDepartmentId
        if (dto.getCurrentDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getCurrentDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getCurrentDepartmentId()));
            asset.setCurrentDepartment(department);
        }

        // Set Employee nếu có currentEmployeeId
        if (dto.getCurrentEmployeeId() != null) {
            Employee employee = employeeRepository.findById(dto.getCurrentEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + dto.getCurrentEmployeeId()));
            asset.setCurrentEmployee(employee);
        }

        return asset;
    }
}
