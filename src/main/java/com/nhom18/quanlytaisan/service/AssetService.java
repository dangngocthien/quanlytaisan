package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetDTO;

import java.util.List;

public interface AssetService {

    /**
     * Lấy danh sách tất cả tài sản
     */
    List<AssetDTO> getAll();

    /**
     * Lấy tài sản theo ID
     */
    AssetDTO getById(Long id);

    /**
     * Tạo tài sản mới
     */
    AssetDTO create(AssetDTO assetDTO);

    /**
     * Cập nhật tài sản
     */
    AssetDTO update(Long id, AssetDTO assetDTO);

    /**
     * Xóa tài sản
     */
    void delete(Long id);

    /**
     * Tìm tài sản theo asset code
     */
    AssetDTO findByAssetCode(String assetCode);

    /**
     * Lấy danh sách tài sản theo category
     */
    List<AssetDTO> findByCategoryId(Long categoryId);

    /**
     * Lấy danh sách tài sản theo phòng ban
     */
    List<AssetDTO> findByCurrentDepartmentId(Long departmentId);

    /**
     * Lấy danh sách tài sản theo trạng thái
     */
    List<AssetDTO> findByStatus(String status);
    
    /**
     * Tra cứu tài sản theo nhiều tiêu chí
     */
    List<AssetDTO> searchAssets(String keyword, Long categoryId, Long departmentId);
}
