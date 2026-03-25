package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.service.AssetCategoryService;
import com.nhom18.quanlytaisan.service.AssetService;
import com.nhom18.quanlytaisan.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     * Hiển thị danh sách tài sản dạng bảng, có hỗ trợ tra cứu
     */
    @GetMapping("/tai-san")
    public String listAssets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long departmentId,
            Model model) {
            
        // Tìm kiếm tài sản dựa trên các tiêu chí (nếu không có thì trả về tất cả)
        model.addAttribute("assets", assetService.searchAssets(keyword, categoryId, departmentId));
        
        // Pushing search params to model to prepopulate search form
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedDepartmentId", departmentId);
        
        // Load các danh sách phụ để làm combobox khi tạo tài sản mới và form tìm kiếm
        model.addAttribute("categories", assetCategoryService.getAll());
        model.addAttribute("departments", departmentService.getAll());
        return "assets";
    }
}
