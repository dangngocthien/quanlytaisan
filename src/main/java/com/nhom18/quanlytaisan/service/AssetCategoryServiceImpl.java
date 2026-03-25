package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetCategoryDTO;
import com.nhom18.quanlytaisan.entity.AssetCategory;
import com.nhom18.quanlytaisan.repository.AssetCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetCategoryServiceImpl implements AssetCategoryService {

    private final AssetCategoryRepository assetCategoryRepository;

    /**
     * Constructor Dependency Injection
     */
    public AssetCategoryServiceImpl(AssetCategoryRepository assetCategoryRepository) {
        this.assetCategoryRepository = assetCategoryRepository;
    }

    @Override
    public List<AssetCategoryDTO> getAll() {
        List<AssetCategory> categories = assetCategoryRepository.findAll();
        return categories.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AssetCategoryDTO getById(Long id) {
        AssetCategory category = assetCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AssetCategory not found with id: " + id));
        return mapEntityToDTO(category);
    }

    @Override
    public AssetCategoryDTO create(AssetCategoryDTO assetCategoryDTO) {
        // Kiểm tra category code đã tồn tại chưa
        if (assetCategoryRepository.existsByCategoryCode(assetCategoryDTO.getCategoryCode())) {
            throw new RuntimeException("AssetCategory code already exists: " + assetCategoryDTO.getCategoryCode());
        }

        // Mapping DTO → Entity
        AssetCategory category = mapDTOToEntity(assetCategoryDTO);

        // Lưu vào database
        AssetCategory savedCategory = assetCategoryRepository.save(category);

        // Mapping Entity → DTO để trả về
        return mapEntityToDTO(savedCategory);
    }

    @Override
    public AssetCategoryDTO update(Long id, AssetCategoryDTO assetCategoryDTO) {
        AssetCategory category = assetCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AssetCategory not found with id: " + id));

        // Cập nhật dữ liệu
        category.setName(assetCategoryDTO.getName());
        category.setDescription(assetCategoryDTO.getDescription());
        category.setDefaultUsefulLifeMonths(assetCategoryDTO.getDefaultUsefulLifeMonths());

        // Nếu category code thay đổi, kiểm tra xem code mới đã tồn tại không
        if (!category.getCategoryCode().equals(assetCategoryDTO.getCategoryCode())) {
            if (assetCategoryRepository.existsByCategoryCode(assetCategoryDTO.getCategoryCode())) {
                throw new RuntimeException("AssetCategory code already exists: " + assetCategoryDTO.getCategoryCode());
            }
            category.setCategoryCode(assetCategoryDTO.getCategoryCode());
        }

        // Lưu cập nhật
        AssetCategory updatedCategory = assetCategoryRepository.save(category);

        // Trả về DTO
        return mapEntityToDTO(updatedCategory);
    }

    @Override
    public void delete(Long id) {
        AssetCategory category = assetCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AssetCategory not found with id: " + id));

        assetCategoryRepository.delete(category);
    }

    @Override
    public AssetCategoryDTO findByCategoryCode(String categoryCode) {
        AssetCategory category = assetCategoryRepository.findByCategoryCode(categoryCode);
        if (category == null) {
            throw new RuntimeException("AssetCategory not found with code: " + categoryCode);
        }
        return mapEntityToDTO(category);
    }

    /**
     * Mapping Entity → DTO (thủ công)
     */
    private AssetCategoryDTO mapEntityToDTO(AssetCategory category) {
        AssetCategoryDTO dto = new AssetCategoryDTO();
        dto.setId(category.getId());
        dto.setCategoryCode(category.getCategoryCode());
        dto.setName(category.getName());
        dto.setDefaultUsefulLifeMonths(category.getDefaultUsefulLifeMonths());
        dto.setDescription(category.getDescription());
        return dto;
    }

    /**
     * Mapping DTO → Entity (thủ công)
     */
    private AssetCategory mapDTOToEntity(AssetCategoryDTO dto) {
        AssetCategory category = new AssetCategory();
        category.setCategoryCode(dto.getCategoryCode());
        category.setName(dto.getName());
        category.setDefaultUsefulLifeMonths(dto.getDefaultUsefulLifeMonths());
        category.setDescription(dto.getDescription());
        category.setCreatedAt(java.time.LocalDateTime.now());
        return category;
    }
}
