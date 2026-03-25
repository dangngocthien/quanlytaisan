package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Tìm phòng ban theo mã code
     */
    Department findByCode(String code);

    /**
     * Kiểm tra xem code có tồn tại không
     */
    boolean existsByCode(String code);
}
