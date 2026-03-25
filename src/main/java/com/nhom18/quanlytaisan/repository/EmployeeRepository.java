package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Tìm nhân viên theo mã code
     */
    Employee findByEmployeeCode(String employeeCode);

    /**
     * Kiểm tra xem mã nhân viên có tồn tại không
     */
    boolean existsByEmployeeCode(String employeeCode);
}
