package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.service.AssetCategoryService;
import com.nhom18.quanlytaisan.service.AssetService;
import com.nhom18.quanlytaisan.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web Controller cho Module Tài sản (Asset)
 * Dùng @Controller để trả về Thymeleaf templates (String)
 */
@Controller
public class AssetWebController {

    private final AssetService assetService;
    private final AssetCategoryService assetCategoryService;
    private final DepartmentService departmentService;

    /**
     * Constructor Dependency Injection
     */
    public AssetWebController(AssetService assetService, 
                              AssetCategoryService assetCategoryService, 
                              DepartmentService departmentService) {
        this.assetService = assetService;
        this.assetCategoryService = assetCategoryService;
        this.departmentService = departmentService;
    }

    /**
     * GET: /tai-san
     * Hiển thị danh sách tài sản dạng bảng
     */
    @GetMapping("/tai-san")
    public String listAssets(Model model) {
        // Lấy danh sách tài sản từ Service
        model.addAttribute("assets", assetService.getAll());
        
        // Load các danh sách phụ để làm combobox khi tạo tài sản mới
        model.addAttribute("categories", assetCategoryService.getAll());
        model.addAttribute("departments", departmentService.getAll());
        return "assets";
    }
}
