package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.DepreciationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository cho Entity DepreciationHistory
 * Senior Spring Boot practice: JpaRepository provides CRUD + custom queries
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
     * Trả về Optional vì một kỳ chỉ có duy nhất một record
     *
     * @param assetId ID của tài sản
     * @param periodMonth Tháng
     * @param periodYear Năm
     * @return Optional chứa lịch sử khấu hao (hoặc rỗng nếu không tồn tại)
     */
    Optional<DepreciationHistory> findByAsset_IdAndPeriodMonthAndPeriodYear(Long assetId, Integer periodMonth, Integer periodYear);

    /**
     * Kiểm tra xem kỳ khấu hao có tồn tại hay không
     * @param assetId ID của tài sản
     * @param periodMonth Tháng
     * @param periodYear Năm
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsByAsset_IdAndPeriodMonthAndPeriodYear(Long assetId, Integer periodMonth, Integer periodYear);
}
