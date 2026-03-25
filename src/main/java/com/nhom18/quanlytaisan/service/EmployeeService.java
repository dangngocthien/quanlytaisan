package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    /**
     * Lấy danh sách tất cả nhân viên
     */
    List<EmployeeDTO> getAll();

    /**
     * Lấy nhân viên theo ID
     */
    EmployeeDTO getById(Long id);

    /**
     * Tạo nhân viên mới
     */
    EmployeeDTO create(EmployeeDTO employeeDTO);

    /**
     * Cập nhật nhân viên
     */
    EmployeeDTO update(Long id, EmployeeDTO employeeDTO);

    /**
     * Xóa nhân viên
     */
    void delete(Long id);

    /**
     * Tìm nhân viên theo employee code
     */
    EmployeeDTO findByEmployeeCode(String employeeCode);
}
