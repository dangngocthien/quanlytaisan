package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface cho tính toán và quản lý khấu hao tài sản
 * Phase 4: Depreciation calculation engine
 * 
 * Công thức khấu hao tuyến tính:
 *   - Khấu hao hàng tháng = Giá mua / Tuổi thọ (tháng)
 *   - Giá trị sổ sách = Giá mua - Khấu hao lũy tích
 * 
 * Tuổi thọ:
 *   - Mỗi tài sản có tuổi thọ riêng (lưu trong cột useful_life_months của entity Asset)
 *   - Nếu không set, mặc định là 36 tháng (3 năm)
 */
public interface DepreciationHistoryService {

    /**
     * Tính khấu hao cho tài sản trong kỳ nhất định
     * Nếu kỳ này đã tồn tại, sẽ cập nhật lại
     *
     * @param assetId ID của tài sản
     * @param month Tháng (1-12)
     * @param year Năm (>= 2000)
     * @return DTO kỳ khấu hao mới
     * @throws RuntimeException nếu tài sản không tồn tại
     * @throws IllegalArgumentException nếu tham số không hợp lệ
     */
    DepreciationHistoryDTO calculateDepreciation(Long assetId, Integer month, Integer year);

    /**
     * Lấy giá trị sổ sách hiện tại của tài sản
     * Công thức: Giá mua - Tổng khấu hao lũy tích
     *
     * @param assetId ID của tài sản
     * @return Giá trị sổ sách (book value)
     */
    BigDecimal getCurrentBookValue(Long assetId);

    /**
     * Lấy tổng khấu hao lũy tích của tài sản
     *
     * @param assetId ID của tài sản
     * @return Tổng khấu hao từ khi mua đến nay
     */
    BigDecimal getTotalAccumulatedDepreciation(Long assetId);

    /**
     * Lấy lịch sử khấu hao của tài sản
     * Sắp xếp theo kỳ gần nhất trước (DESC)
     *
     * @param assetId ID của tài sản
     * @return Danh sách kỳ khấu hao
     */
    List<DepreciationHistoryDTO> getDepreciationsByAsset(Long assetId);

    /**
     * Lấy kỳ khấu hao cụ thể
     *
     * @param assetId ID của tài sản
     * @param month Tháng (1-12)
     * @param year Năm
     * @return DTO kỳ khấu hao, hoặc Optional.empty() nếu không tồn tại
     */
    java.util.Optional<DepreciationHistoryDTO> getDepreciationByPeriod(Long assetId, Integer month, Integer year);

    /**
     * Lấy tất cả kỳ khấu hao trong một năm
     *
     * @param year Năm
     * @return Danh sách kỳ khấu hao
     */
    List<DepreciationHistoryDTO> getDepreciationsByYear(Integer year);

    /**
     * Xóa kỳ khấu hao cụ thể
     *
     * @param depreciationId ID kỳ khấu hao
     * @return true nếu xóa thành công
     */
    boolean deleteDepreciation(Long depreciationId);

    /**
     * Lấy chi tiết một bản ghi khấu hao theo ID
     *
     * @param depreciationId ID bản ghi khấu hao
     * @return DTO bản ghi khấu hao
     * @throws RuntimeException nếu không tìm thấy
     */
    DepreciationHistoryDTO getDepreciationById(Long depreciationId);

    /**
     * Tính khấu hao cho tất cả tài sản trong một tháng
     * Bỏ qua tài sản không thể tính được (không có category, đã tính rồi, v.v.)
     *
     * @param month Tháng (1-12)
     * @param year Năm
     * @return Danh sách kỳ khấu hao đã tính
     */
    List<DepreciationHistoryDTO> calculateAllAssetsDepreciation(Integer month, Integer year);

    /**
     * Xóa tất cả kỳ khấu hao của tài sản
     * (Thường gọi khi xóa tài sản)
     *
     * @param assetId ID của tài sản
     * @return Số lượng kỳ đã xóa
     */
    long deleteDepreciationsForAsset(Long assetId);
}
