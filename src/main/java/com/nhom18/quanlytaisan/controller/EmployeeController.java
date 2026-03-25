package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.EmployeeDTO;
import com.nhom18.quanlytaisan.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Constructor Dependency Injection
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET: /api/employees
     * Lấy danh sách tất cả nhân viên
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAll() {
        List<EmployeeDTO> employees = employeeService.getAll();
        return ResponseEntity.ok(employees);
    }

    /**
     * GET: /api/employees/{id}
     * Lấy nhân viên theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * POST: /api/employees
     * Tạo nhân viên mới
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.create(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    /**
     * PUT: /api/employees/{id}
     * Cập nhật nhân viên
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(
            @PathVariable Long id,
            @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployee = employeeService.update(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * DELETE: /api/employees/{id}
     * Xóa nhân viên
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET: /api/employees/code/{employeeCode}
     * Tìm nhân viên theo code
     */
    @GetMapping("/code/{employeeCode}")
    public ResponseEntity<EmployeeDTO> findByEmployeeCode(@PathVariable String employeeCode) {
        EmployeeDTO employee = employeeService.findByEmployeeCode(employeeCode);
        return ResponseEntity.ok(employee);
    }
}
