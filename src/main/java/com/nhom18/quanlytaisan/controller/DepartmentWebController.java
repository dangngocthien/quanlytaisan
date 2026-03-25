package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DepartmentWebController {

    private final DepartmentService departmentService;

    /**
     * Constructor Dependency Injection
     */
    public DepartmentWebController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * GET: /phong-ban
     * Hiển thị danh sách phòng ban
     */
    @GetMapping("/phong-ban")
    public String listDepartments(Model model, HttpServletRequest request) {
        // Lấy danh sách phòng ban từ Service
        model.addAttribute("departments", departmentService.getAll());
        
        // Thêm context path để JavaScript có thể sử dụng
        model.addAttribute("contextPath", request.getContextPath());
        
        // Trả về tên view (Thymeleaf sẽ tìm file templates/departments.html)
        return "departments";
    }
}
