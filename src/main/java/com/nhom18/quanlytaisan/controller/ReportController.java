package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.AssetDTO;
import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import com.nhom18.quanlytaisan.service.ExcelExportService;
import com.nhom18.quanlytaisan.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller cho API báo cáo quản lý tài sản
 * Phase 3: Basic asset reports
 * Phase 4: Depreciation & Valuation reports (Mới)
 * Base URL: /api/reports
 *
 * Áp dụng senior Spring Boot patterns:
 * - Constructor injection
 * - ResponseEntity<> cho responses
 * - Proper HTTP status codes
 * - Exception handling tập trung
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;
    private final ExcelExportService excelExportService;

    /**
     * Constructor injection - Senior Spring Boot practice
     */
    public ReportController(ReportService reportService, ExcelExportService excelExportService) {
        this.reportService = reportService;
        this.excelExportService = excelExportService;
    }

    // ============ PHASE 3: BASIC ASSET REPORTS ============

    /**
     * GET /api/reports/assets-by-department/{departmentId}
     * Lấy danh sách tài sản theo phòng ban
     */
    @GetMapping("/assets-by-department/{departmentId}")
    public ResponseEntity<?> getAssetsByDepartment(@PathVariable Long departmentId) {
        try {
            List<AssetDTO> assets = reportService.getAssetsByDepartment(departmentId);
            Map<String, Object> response = new HashMap<>();
            response.put("departmentId", departmentId);
            response.put("totalCount", assets.size());
            response.put("data", assets);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi lấy tài sản theo phòng ban", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/assets-by-category/{categoryId}
     * Lấy danh sách tài sản theo danh mục
     */
    @GetMapping("/assets-by-category/{categoryId}")
    public ResponseEntity<?> getAssetsByCategory(@PathVariable Long categoryId) {
        try {
            List<AssetDTO> assets = reportService.getAssetsByCategory(categoryId);
            Map<String, Object> response = new HashMap<>();
            response.put("categoryId", categoryId);
            response.put("totalCount", assets.size());
            response.put("data", assets);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi lấy tài sản theo danh mục", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/assets-by-status
     * Lấy danh sách tài sản theo trạng thái
     */
    @GetMapping("/assets-by-status")
    public ResponseEntity<?> getAssetsByStatus(@RequestParam String status) {
        try {
            List<AssetDTO> assets = reportService.getAssetsByStatus(status);
            Map<String, Object> response = new HashMap<>();
            response.put("status", status);
            response.put("totalCount", assets.size());
            response.put("data", assets);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi lấy tài sản theo trạng thái", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/all-assets
     * Lấy danh sách tất cả tài sản
     */
    @GetMapping("/all-assets")
    public ResponseEntity<?> getAllAssets() {
        try {
            List<AssetDTO> assets = reportService.getAllAssets();
            Map<String, Object> response = new HashMap<>();
            response.put("totalCount", assets.size());
            response.put("data", assets);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi lấy danh sách tài sản", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/total-value-by-department/{departmentId}
     * Lấy tổng giá trị tài sản theo phòng ban (giá hiện tại)
     */
    @GetMapping("/total-value-by-department/{departmentId}")
    public ResponseEntity<?> getTotalValueByDepartment(@PathVariable Long departmentId) {
        try {
            Double totalValue = reportService.getTotalValueByDepartment(departmentId);
            Map<String, Object> response = new HashMap<>();
            response.put("departmentId", departmentId);
            response.put("totalCurrentValue", totalValue);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi tính tổng giá trị", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/total-value-by-category/{categoryId}
     * Lấy tổng giá trị tài sản theo danh mục (giá hiện tại)
     */
    @GetMapping("/total-value-by-category/{categoryId}")
    public ResponseEntity<?> getTotalValueByCategory(@PathVariable Long categoryId) {
        try {
            Double totalValue = reportService.getTotalValueByCategory(categoryId);
            Map<String, Object> response = new HashMap<>();
            response.put("categoryId", categoryId);
            response.put("totalCurrentValue", totalValue);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi tính tổng giá trị", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/dashboard
     * Lấy thông tin tổng quan (Dashboard)
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        try {
            List<AssetDTO> allAssets = reportService.getAllAssets();

            Double totalCurrentValue = allAssets.stream()
                    .map(AssetDTO::getCurrentValue)
                    .reduce(
                            BigDecimal.ZERO,
                            BigDecimal::add
                    )
                    .doubleValue();

            Double totalPurchaseValue = allAssets.stream()
                    .map(AssetDTO::getPurchasePrice)
                    .reduce(
                            BigDecimal.ZERO,
                            BigDecimal::add
                    )
                    .doubleValue();

            Long totalAssets = (long) allAssets.size();

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
            response.put("totalCurrentValue", totalCurrentValue);
            response.put("totalPurchaseValue", totalPurchaseValue);
            response.put("activeAssets", activeAssets);
            response.put("maintenanceAssets", maintenanceAssets);
            response.put("disposedAssets", disposedAssets);
            response.put("depreciationTotal", totalPurchaseValue - totalCurrentValue);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi lấy dashboard", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/dashboard/departments
     * Báo cáo tổng tài sản và giá trị theo từng phòng ban
     */
    @GetMapping("/dashboard/departments")
    public ResponseEntity<?> getDashboardByDepartments() {
        try {
            List<AssetDTO> allAssets = reportService.getAllAssets();
            
            Map<String, List<AssetDTO>> assetsByDept = allAssets.stream()
                .filter(a -> a.getCurrentDepartmentName() != null)
                .collect(java.util.stream.Collectors.groupingBy(AssetDTO::getCurrentDepartmentName));

            List<Map<String, Object>> deptStats = assetsByDept.entrySet().stream()
                .map(entry -> {
                    String deptName = entry.getKey();
                    List<AssetDTO> deptAssets = entry.getValue();
                    long count = deptAssets.size();
                    BigDecimal totalVal = deptAssets.stream()
                        .map(a -> a.getCurrentValue() != null ? a.getCurrentValue() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                        
                    Map<String, Object> stat = new HashMap<>();
                    stat.put("departmentName", deptName);
                    stat.put("assetCount", count);
                    stat.put("totalValue", totalVal);
                    return stat;
                })
                .collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(deptStats);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi lấy dữ liệu dashboard theo phòng ban", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ============ PHASE 4: DEPRECIATION & VALUATION REPORTS ============

    /**
     * GET /api/reports/depreciation/history/{assetId}
     * Lấy lịch sử khấu hao của tài sản
     */
    @GetMapping("/depreciation/history/{assetId}")
    public ResponseEntity<?> getAssetDepreciationHistory(@PathVariable Long assetId) {
        try {
            List<DepreciationHistoryDTO> histories = reportService.getAssetDepreciationHistory(assetId);
            Map<String, Object> response = new HashMap<>();
            response.put("assetId", assetId);
            response.put("totalRecords", histories.size());
            response.put("data", histories);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi lấy lịch sử khấu hao", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/valuation/{assetId}
     * Lấy báo cáo định giá chi tiết của tài sản
     * Bao gồm: giá mua, khấu hao lũy tích, giá trị sổ sách, tỷ lệ khấu hao
     */
    @GetMapping("/valuation/{assetId}")
    public ResponseEntity<?> getAssetValuationReport(@PathVariable Long assetId) {
        try {
            Map<String, Object> report = reportService.getAssetValuationReport(assetId);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi tạo báo cáo định giá", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/department-valuation/{departmentId}
     * Lấy báo cáo định giá của tất cả tài sản trong phòng ban
     */
    @GetMapping("/department-valuation/{departmentId}")
    public ResponseEntity<?> getDepartmentValuationReport(@PathVariable Long departmentId) {
        try {
            List<Map<String, Object>> reports = reportService.getDepartmentValuationReport(departmentId);
            Map<String, Object> response = new HashMap<>();
            response.put("departmentId", departmentId);
            response.put("totalAssets", reports.size());
            response.put("data", reports);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi tạo báo cáo định giá phòng ban", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/valuation/export-all
     * Xuất báo cáo định giá toàn bộ tài sản ra Excel
     */
    @GetMapping("/valuation/export-all")
    public ResponseEntity<byte[]> exportAllValuationReport() {
        try {
            List<Map<String, Object>> reports = reportService.getAllValuationReports();
            byte[] excelContent = excelExportService.exportValuationReport(reports, "Báo Cáo Định Giá Tổng Hợp");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "BaoCaoDinhGia_ToanBo_" + System.currentTimeMillis() + ".xlsx");

            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/reports/department-valuation/{departmentId}/export
     * Xuất báo cáo định giá theo phòng ban ra Excel
     */
    @GetMapping("/department-valuation/{departmentId}/export")
    public ResponseEntity<byte[]> exportDepartmentValuationReport(@PathVariable Long departmentId) {
        try {
            List<Map<String, Object>> reports = reportService.getDepartmentValuationReport(departmentId);
            byte[] excelContent = excelExportService.exportValuationReport(reports, "Báo Cáo Định Giá Phòng Ban");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "BaoCaoDinhGia_PhongBan_" + departmentId + "_" + System.currentTimeMillis() + ".xlsx");

            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/reports/book-value-by-department/{departmentId}
     * Lấy tổng giá trị sổ sách của tài sản trong phòng ban
     * Công thức: Tổng (Giá mua - Khấu hao lũy tích)
     */
    @GetMapping("/book-value-by-department/{departmentId}")
    public ResponseEntity<?> getTotalBookValueByDepartment(@PathVariable Long departmentId) {
        try {
            BigDecimal totalBookValue = reportService.getTotalBookValueByDepartment(departmentId);
            Map<String, Object> response = new HashMap<>();
            response.put("departmentId", departmentId);
            response.put("totalBookValue", totalBookValue);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi tính tổng giá trị sổ sách", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/depreciation-summary/{year}
     * Lấy tóm tắt khấu hao theo năm (theo tháng)
     */
    @GetMapping("/depreciation-summary/{year}")
    public ResponseEntity<?> getDepreciationSummaryByYear(@PathVariable Integer year) {
        try {
            Map<Integer, Map<String, Object>> summary = reportService.getDepreciationSummaryByYear(year);
            Map<String, Object> response = new HashMap<>();
            response.put("year", year);
            response.put("totalMonths", summary.size());
            response.put("data", summary);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Lỗi khi tạo tóm tắt khấu hao năm", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /api/reports/depreciation-summary/{year}/export
     * Xuất tóm tắt khấu hao năm ra file Excel
     */
    @GetMapping("/depreciation-summary/{year}/export")
    public ResponseEntity<byte[]> exportDepreciationSummaryByYear(@PathVariable Integer year) {
        try {
            Map<Integer, Map<String, Object>> summary = reportService.getDepreciationSummaryByYear(year);
            byte[] excelContent = excelExportService.exportDepreciationSummary(summary, year);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "BaoCaoKhauHao_Nam_" + year + "_" + System.currentTimeMillis() + ".xlsx");

            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ HELPER METHOD ============

    /**
     * Tạo response lỗi chuẩn
     */
    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return ResponseEntity.status(status).body(response);
    }
}
