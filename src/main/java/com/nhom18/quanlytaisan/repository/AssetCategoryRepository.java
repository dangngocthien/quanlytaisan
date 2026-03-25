package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.AssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetCategoryRepository extends JpaRepository<AssetCategory, Long> {

    /**
     * Tìm danh mục tài sản theo mã code
     */
    AssetCategory findByCategoryCode(String categoryCode);

    /**
     * Kiểm tra xem mã category có tồn tại không
     */
    boolean existsByCategoryCode(String categoryCode);
}
