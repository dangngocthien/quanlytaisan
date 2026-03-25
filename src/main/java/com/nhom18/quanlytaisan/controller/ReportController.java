package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.AssetDTO;
import com.nhom18.quanlytaisan.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller cho API báo cáo quản lý tài sản
 * Base URL: /api/reports
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * GET /api/reports/assets-by-department/{departmentId}
     * Lấy danh sách tài sản theo phòng ban
     * @param departmentId ID của phòng ban
     * @return Danh sách tài sản (DTO)
     */
    @GetMapping("/assets-by-department/{departmentId}")
    public List<AssetDTO> getAssetsByDepartment(@PathVariable Long departmentId) {
        return reportService.getAssetsByDepartment(departmentId);
    }

    /**
     * GET /api/reports/assets-by-category/{categoryId}
     * Lấy danh sách tài sản theo danh mục
     * @param categoryId ID của danh mục
     * @return Danh sách tài sản (DTO)
     */
    @GetMapping("/assets-by-category/{categoryId}")
    public List<AssetDTO> getAssetsByCategory(@PathVariable Long categoryId) {
        return reportService.getAssetsByCategory(categoryId);
    }

    /**
     * GET /api/reports/assets-by-status
     * Lấy danh sách tài sản theo trạng thái
     * @param status Trạng thái của tài sản (VD: "DANG_DUNG", "BAO_TRI", "THANH_LY")
     * @return Danh sách tài sản (DTO)
     */
    @GetMapping("/assets-by-status")
    public List<AssetDTO> getAssetsByStatus(@RequestParam String status) {
        return reportService.getAssetsByStatus(status);
    }

    /**
     * GET /api/reports/all-assets
     * Lấy danh sách tất cả tài sản
     * @return Danh sách tất cả tài sản (DTO)
     */
    @GetMapping("/all-assets")
    public List<AssetDTO> getAllAssets() {
        return reportService.getAllAssets();
    }

    /**
     * GET /api/reports/total-value-by-department/{departmentId}
     * Lấy tổng giá trị tài sản theo phòng ban
     * @param departmentId ID của phòng ban
     * @return Object chứa tổng giá trị
     */
    @GetMapping("/total-value-by-department/{departmentId}")
    public Map<String, Object> getTotalValueByDepartment(@PathVariable Long departmentId) {
        Double totalValue = reportService.getTotalValueByDepartment(departmentId);
        Map<String, Object> response = new HashMap<>();
        response.put("departmentId", departmentId);
        response.put("totalValue", totalValue);
        return response;
    }

    /**
     * GET /api/reports/total-value-by-category/{categoryId}
     * Lấy tổng giá trị tài sản theo danh mục
     * @param categoryId ID của danh mục
     * @return Object chứa tổng giá trị
     */
    @GetMapping("/total-value-by-category/{categoryId}")
    public Map<String, Object> getTotalValueByCategory(@PathVariable Long categoryId) {
        Double totalValue = reportService.getTotalValueByCategory(categoryId);
        Map<String, Object> response = new HashMap<>();
        response.put("categoryId", categoryId);
        response.put("totalValue", totalValue);
        return response;
    }

    /**
     * GET /api/reports/dashboard
     * Lấy thông tin tổng quan (Dashboard)
     * @return Object chứa các thống kê chung
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        List<AssetDTO> allAssets = reportService.getAllAssets();
        
        Double totalValue = allAssets.stream()
                .map(AssetDTO::getCurrentValue)
                .reduce(
                        java.math.BigDecimal.ZERO,
                        java.math.BigDecimal::add
                )
                .doubleValue();

        Double totalPurchaseValue = allAssets.stream()
                .map(AssetDTO::getPurchasePrice)
                .reduce(
                        java.math.BigDecimal.ZERO,
                        java.math.BigDecimal::add
                )
                .doubleValue();

        Long totalAssets = (long) allAssets.size();
        
        // Đếm theo trạng thái
        Long activeAssets = allAssets.stream()
                .filter(asset -> "DANG_DUNG".equalsIgnoreCase(asset.getStatus()))
                .count();
        
        Long maintenanceAssets = allAssets.stream()
                .filter(asset -> "BAO_TRI".equalsIgnoreCase(asset.getStatus()))
                .count();
        
        Long disposedAssets = allAssets.stream()
                .filter(asset -> "THANH_LY".equalsIgnoreCase(asset.getStatus()))
                .count();

        Map<String, Object> response = new HashMap<>();
        response.put("totalAssets", totalAssets);
        response.put("totalCurrentValue", totalValue);
        response.put("totalPurchaseValue", totalPurchaseValue);
        response.put("activeAssets", activeAssets);
        response.put("maintenanceAssets", maintenanceAssets);
        response.put("disposedAssets", disposedAssets);
        response.put("depreciationTotal", totalPurchaseValue - totalValue);

        return response;
    }
}
