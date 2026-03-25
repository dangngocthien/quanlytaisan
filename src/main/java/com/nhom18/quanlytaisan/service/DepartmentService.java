package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    /**
     * Lấy danh sách tất cả phòng ban
     */
    List<DepartmentDTO> getAll();

    /**
     * Lấy phòng ban theo ID
     */
    DepartmentDTO getById(Long id);

    /**
     * Tạo phòng ban mới
     */
    DepartmentDTO create(DepartmentDTO departmentDTO);

    /**
     * Cập nhật phòng ban
     */
    DepartmentDTO update(Long id, DepartmentDTO departmentDTO);

    /**
     * Xóa phòng ban
     */
    void delete(Long id);

    /**
     * Tìm phòng ban theo code
     */
    DepartmentDTO findByCode(String code);
}
