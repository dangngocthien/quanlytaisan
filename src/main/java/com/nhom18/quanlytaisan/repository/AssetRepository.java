package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * Tìm tài sản theo mã asset code
     */
    Asset findByAssetCode(String assetCode);

    /**
     * Kiểm tra xem mã tài sản có tồn tại không
     */
    boolean existsByAssetCode(String assetCode);

    /**
     * Lấy danh sách tài sản theo danh mục
     */
    List<Asset> findByCategoryId(Long categoryId);

    /**
     * Lấy danh sách tài sản theo phòng ban
     */
    List<Asset> findByCurrentDepartmentId(Long departmentId);

    /**
     * Lấy danh sách tài sản theo trạng thái
     */
    List<Asset> findByStatus(String status);
    
    /**
     * Tra cứu tài sản theo nhiều tiêu chí
     */
    @org.springframework.data.jpa.repository.Query("SELECT a FROM Asset a " +
            "WHERE (LOWER(a.assetCode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:categoryId IS NULL OR a.category.id = :categoryId) " +
            "AND (:departmentId IS NULL OR a.currentDepartment.id = :departmentId)")
    List<Asset> searchAssets(@org.springframework.data.repository.query.Param("keyword") String keyword,
                             @org.springframework.data.repository.query.Param("categoryId") Long categoryId,
                             @org.springframework.data.repository.query.Param("departmentId") Long departmentId);

    /**
     * Lấy danh sách tài sản sắp đến hạn bảo trì
     */
    List<Asset> findByNextMaintenanceDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
