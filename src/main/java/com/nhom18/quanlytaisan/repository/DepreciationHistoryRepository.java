package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.DepreciationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository cho Entity DepreciationHistory
 */
@Repository
public interface DepreciationHistoryRepository extends JpaRepository<DepreciationHistory, Long> {

    /**
     * Tìm kiếm lịch sử khấu hao theo Asset ID
     * @param assetId ID của tài sản
     * @return Danh sách lịch sử khấu hao
     */
    List<DepreciationHistory> findByAsset_Id(Long assetId);

    /**
     * Tìm kiếm lịch sử khấu hao theo năm tháng
     * @param periodMonth Tháng
     * @param periodYear Năm
     * @return Danh sách lịch sử khấu hao
     */
    List<DepreciationHistory> findByPeriodMonthAndPeriodYear(Integer periodMonth, Integer periodYear);

    /**
     * Tìm kiếm lịch sử khấu hao của asset theo năm tháng cụ thể
     * @param assetId ID của tài sản
     * @param periodMonth Tháng
     * @param periodYear Năm
     * @return Danh sách lịch sử khấu hao
     */
    List<DepreciationHistory> findByAsset_IdAndPeriodMonthAndPeriodYear(Long assetId, Integer periodMonth, Integer periodYear);
}
