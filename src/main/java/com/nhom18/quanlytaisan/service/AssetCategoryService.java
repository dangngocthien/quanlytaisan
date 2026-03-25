package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetCategoryDTO;

import java.util.List;

public interface AssetCategoryService {

    /**
     * Lấy danh sách tất cả danh mục tài sản
     */
    List<AssetCategoryDTO> getAll();

    /**
     * Lấy danh mục tài sản theo ID
     */
    AssetCategoryDTO getById(Long id);

    /**
     * Tạo danh mục tài sản mới
     */
    AssetCategoryDTO create(AssetCategoryDTO assetCategoryDTO);

    /**
     * Cập nhật danh mục tài sản
     */
    AssetCategoryDTO update(Long id, AssetCategoryDTO assetCategoryDTO);

    /**
     * Xóa danh mục tài sản
     */
    void delete(Long id);

    /**
     * Tìm danh mục tài sản theo category code
     */
    AssetCategoryDTO findByCategoryCode(String categoryCode);
}
