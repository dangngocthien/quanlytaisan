package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetDTO;
import java.util.List;

/**
 * Service interface cho quản lý báo cáo
 */
public interface ReportService {

    /**
     * Lấy danh sách tài sản theo phòng ban
     * @param departmentId ID của phòng ban
     * @return Danh sách tài sản (DTO)
     */
    List<AssetDTO> getAssetsByDepartment(Long departmentId);

    /**
     * Lấy danh sách tài sản theo danh mục
     * @param categoryId ID của danh mục
     * @return Danh sách tài sản (DTO)
     */
    List<AssetDTO> getAssetsByCategory(Long categoryId);

    /**
     * Lấy danh sách tài sản theo trạng thái
     * @param status Trạng thái của tài sản (VD: "DANG_DUNG", "BAO_TRI", "THANH_LY")
     * @return Danh sách tài sản (DTO)
     */
    List<AssetDTO> getAssetsByStatus(String status);

    /**
     * Lấy danh sách tất cả tài sản
     * @return Danh sách tất cả tài sản (DTO)
     */
    List<AssetDTO> getAllAssets();

    /**
     * Lấy tổng giá trị tài sản theo phòng ban
     * @param departmentId ID của phòng ban
     * @return Tổng giá trị tài sản
     */
    Double getTotalValueByDepartment(Long departmentId);

    /**
     * Lấy tổng giá trị tài sản theo danh mục
     * @param categoryId ID của danh mục
     * @return Tổng giá trị tài sản
     */
    Double getTotalValueByCategory(Long categoryId);
}
