package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.DepartmentDTO;
import com.nhom18.quanlytaisan.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Constructor Dependency Injection
     */
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * GET: /api/departments
     * Lấy danh sách tất cả phòng ban
     */
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAll() {
        List<DepartmentDTO> departments = departmentService.getAll();
        return ResponseEntity.ok(departments);
    }

    /**
     * GET: /api/departments/{id}
     * Lấy phòng ban theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getById(@PathVariable Long id) {
        DepartmentDTO department = departmentService.getById(id);
        return ResponseEntity.ok(department);
    }

    /**
     * POST: /api/departments
     * Tạo phòng ban mới
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> create(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.create(departmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
    }

    /**
     * PUT: /api/departments/{id}
     * Cập nhật phòng ban
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> update(
            @PathVariable Long id,
            @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO updatedDepartment = departmentService.update(id, departmentDTO);
        return ResponseEntity.ok(updatedDepartment);
    }

    /**
     * DELETE: /api/departments/{id}
     * Xóa phòng ban
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET: /api/departments/code/{code}
     * Tìm phòng ban theo code
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<DepartmentDTO> findByCode(@PathVariable String code) {
        DepartmentDTO department = departmentService.findByCode(code);
        return ResponseEntity.ok(department);
    }
}
