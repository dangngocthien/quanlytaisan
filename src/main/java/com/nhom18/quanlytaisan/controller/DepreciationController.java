package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import com.nhom18.quanlytaisan.service.DepreciationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller cho API quản lý khấu hao tài sản
 * Base URL: /api/depreciation
 */
@RestController
@RequestMapping("/api/depreciation")
@CrossOrigin(origins = "*")
public class DepreciationController {

    @Autowired
    private DepreciationService depreciationService;

    /**
     * GET /api/depreciation/history/{assetId}
     * Lấy lịch sử khấu hao của một tài sản
     * @param assetId ID của tài sản
     * @return Danh sách lịch sử khấu hao
     */
    @GetMapping("/history/{assetId}")
    public List<DepreciationHistoryDTO> getDepreciationHistory(@PathVariable Long assetId) {
        return depreciationService.getHistory(assetId);
    }

    /**
     * GET /api/depreciation/{id}
     * Lấy chi tiết một bản ghi khấu hao theo ID
     * @param id ID của bản ghi khấu hao
     * @return DTO của bản ghi khấu hao
     */
    @GetMapping("/{id}")
    public DepreciationHistoryDTO getDepreciationById(@PathVariable Long id) {
        return depreciationService.getDepreciationById(id);
    }

    /**
     * POST /api/depreciation/calculate
     * Tính khấu hao hàng tháng cho một tài sản
     * 
     * Request Parameters:
     *   - assetId: ID của tài sản cần tính khấu hao
     *   - month: Tháng (1-12)
     *   - year: Năm (VD: 2024)
     * 
     * Example: POST /api/depreciation/calculate?assetId=1&month=3&year=2024
     * 
     * Response: DTO của bản ghi khấu hao vừa tạo
     * 
     * @param assetId ID của tài sản
     * @param month Tháng
     * @param year Năm
     * @return DTO của bản ghi khấu hao vừa tạo
     */
    @PostMapping("/calculate")
    public DepreciationHistoryDTO calculateMonthlyDepreciation(
            @RequestParam Long assetId,
            @RequestParam int month,
            @RequestParam int year) {
        return depreciationService.calculateMonthlyDepreciation(assetId, month, year);
    }

    /**
     * POST /api/depreciation/calculate-all
     * Tính khấu hao cho tất cả tài sản trong một tháng
     * 
     * Request Parameters:
     *   - month: Tháng (1-12)
     *   - year: Năm (VD: 2024)
     * 
     * Example: POST /api/depreciation/calculate-all?month=3&year=2024
     * 
     * Response: Danh sách các bản ghi khấu hao vừa tạo
     * 
     * @param month Tháng
     * @param year Năm
     * @return Danh sách các bản ghi khấu hao vừa tạo
     */
    @PostMapping("/calculate-all")
    public List<DepreciationHistoryDTO> calculateAllAssetsDepreciation(
            @RequestParam int month,
            @RequestParam int year) {
        return depreciationService.calculateAllAssetsDepreciation(month, year);
    }
}
