package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import java.util.List;

/**
 * Service interface cho quản lý khấu hao tài sản
 */
public interface DepreciationService {

    /**
     * Lấy lịch sử khấu hao của một tài sản
     * @param assetId ID của tài sản
     * @return Danh sách lịch sử khấu hao (DTO)
     */
    List<DepreciationHistoryDTO> getHistory(Long assetId);

    /**
     * Tính khấu hao hàng tháng cho một tài sản
     * 
     * Logic:
     *   - Lấy Asset theo ID
     *   - Lấy category của Asset, lấy defaultUsefulLifeMonths
     *   - Tính tiền khấu hao 1 tháng = purchasePrice / defaultUsefulLifeMonths
     *   - Cập nhật currentValue = currentValue - depreciationAmount
     *   - Lưu vào bảng DepreciationHistory
     *   - Cập nhật lại Asset trong DB
     *   - Trả về DTO
     * 
     * @param assetId ID của tài sản cần tính khấu hao
     * @param month Tháng (1-12)
     * @param year Năm
     * @return DTO của bản ghi khấu hao vừa tạo
     */
    DepreciationHistoryDTO calculateMonthlyDepreciation(Long assetId, int month, int year);

    /**
     * Lấy chi tiết một bản ghi khấu hao theo ID
     * @param id ID của bản ghi khấu hao
     * @return DTO của bản ghi khấu hao
     */
    DepreciationHistoryDTO getDepreciationById(Long id);

    /**
     * Tính khấu hao cho tất cả tài sản trong một tháng
     * @param month Tháng
     * @param year Năm
     * @return Danh sách các bản ghi khấu hao vừa tạo
     */
    List<DepreciationHistoryDTO> calculateAllAssetsDepreciation(int month, int year);
}
