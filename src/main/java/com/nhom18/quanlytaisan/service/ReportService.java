package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetDTO;
import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Service interface cho quản lý báo cáo
 * Phase 3: Basic Asset reports
 * Phase 4: Depreciation & Valuation reports (Mới)
 */
public interface ReportService {

    // ============ PHASE 3: BASIC ASSET REPORTS ============

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

    // ============ PHASE 4: DEPRECIATION & VALUATION REPORTS ============

    /**
     * Lấy lịch sử khấu hao của tài sản
     * @param assetId ID tài sản
     * @return Danh sách lịch sử khấu hao, sắp xếp theo kỳ gần nhất trước
     */
    List<DepreciationHistoryDTO> getAssetDepreciationHistory(Long assetId);

    /**
     * Lấy giá trị sổ sách hiện tại của tài sản
     * Công thức: Giá mua - Khấu hao lũy tích
     * @param assetId ID tài sản
     * @return Giá trị sổ sách (book value)
     */
    BigDecimal getAssetBookValue(Long assetId);

    /**
     * Lấy báo cáo định giá chi tiết của tài sản
     * Bao gồm: giá mua, khấu hao lũy tích, giá trị sổ sách, tỷ lệ khấu hao
     * @param assetId ID tài sản
     * @return Map chứa thông tin định giá
     */
    Map<String, Object> getAssetValuationReport(Long assetId);

    /**
     * Lấy báo cáo định giá của tất cả tài sản trong phòng ban
     * @param departmentId ID phòng ban
     * @return Danh sách báo cáo định giá theo tài sản
     */
    List<Map<String, Object>> getDepartmentValuationReport(Long departmentId);

    /**
     * Lấy tổng giá trị sổ sách của tài sản trong phòng ban
     * Công thức: Tổng (Giá mua - Khấu hao lũy tích) cho tất cả tài sản trong phòng
     * @param departmentId ID phòng ban
     * @return Tổng giá trị sổ sách
     */
    BigDecimal getTotalBookValueByDepartment(Long departmentId);

    /**
     * Lấy tóm tắt khấu hao theo năm
     * @param year Năm cần báo cáo (VD: 2024)
     * @return Map chứa tóm tắt theo tháng
     */
    Map<Integer, Map<String, Object>> getDepreciationSummaryByYear(Integer year);
}
