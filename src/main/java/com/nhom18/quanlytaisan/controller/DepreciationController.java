package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.DepreciationHistoryDTO;
import com.nhom18.quanlytaisan.service.DepreciationHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller cho API quản lý khấu hao tài sản
 * Phase 4: Depreciation calculation & management
 * Base URL: /api/depreciation
 *
 * Áp dụng senior Spring Boot patterns:
 * - Constructor injection
 * - ResponseEntity<> cho responses
 * - Proper HTTP status codes
 * - Exception handling tập trung
 */
@RestController
@RequestMapping("/api/depreciation")
@CrossOrigin(origins = "*")
public class DepreciationController {

    private final DepreciationHistoryService depreciationHistoryService;

    /**
     * Constructor injection - Senior Spring Boot practice
     */
    public DepreciationController(DepreciationHistoryService depreciationHistoryService) {
        this.depreciationHistoryService = depreciationHistoryService;
    }

    /**
     * GET /api/depreciation/history/{assetId}
     * Lấy lịch sử khấu hao của một tài sản
     *
     * @param assetId ID của tài sản
     * @return ResponseEntity chứa danh sách lịch sử khấu hao
     */
    @GetMapping("/history/{assetId}")
    public ResponseEntity<?> getDepreciationHistory(@PathVariable Long assetId) {
        try {
            List<DepreciationHistoryDTO> histories = depreciationHistoryService.getDepreciationsByAsset(assetId);
            return ResponseEntity.ok(histories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Lỗi khi lấy lịch sử khấu hao: " + e.getMessage()));
        }
    }

    /**
     * GET /api/depreciation/{id}
     * Lấy chi tiết một bản ghi khấu hao theo ID
     *
     * @param id ID của bản ghi khấu hao
     * @return ResponseEntity chứa DTO of chi tiết khấu hao
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDepreciationById(@PathVariable Long id) {
        try {
            DepreciationHistoryDTO dto = depreciationHistoryService.getDepreciationById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Lỗi: " + e.getMessage()));
        }
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
     * @param assetId ID của tài sản
     * @param month Tháng (1-12)
     * @param year Năm
     * @return ResponseEntity chứa DTO of bản ghi khấu hao mới tạo
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMonthlyDepreciation(
            @RequestParam Long assetId,
            @RequestParam int month,
            @RequestParam int year) {
        try {
            DepreciationHistoryDTO result = depreciationHistoryService.calculateDepreciation(assetId, month, year);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tính khấu hao thành công");
            response.put("data", result);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Tham số không hợp lệ: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Lỗi: " + e.getMessage()));
        }
    }

    /**
     * POST /api/depreciation/calculate-all
     * Tính khấu hao cho tất cả tài sản trong một tháng
     * Bỏ qua tài sản không thể tính được (không có category, đã tính rồi, v.v.)
     *
     * Request Parameters:
     *   - month: Tháng (1-12)
     *   - year: Năm (VD: 2024)
     *
     * Example: POST /api/depreciation/calculate-all?month=3&year=2024
     *
     * @param month Tháng (1-12)
     * @param year Năm
     * @return ResponseEntity chứa danh sách các bản ghi khấu hao mới tạo
     */
    @PostMapping("/calculate-all")
    public ResponseEntity<?> calculateAllAssetsDepreciation(
            @RequestParam int month,
            @RequestParam int year) {
        try {
            List<DepreciationHistoryDTO> results = depreciationHistoryService.calculateAllAssetsDepreciation(month, year);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tính khấu hao cho tất cả tài sản thành công");
            response.put("totalCalculated", results.size());
            response.put("data", results);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Tham số không hợp lệ: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Lỗi: " + e.getMessage()));
        }
    }

    /**
     * GET /api/depreciation/book-value/{assetId}
     * Lấy giá trị sổ sách hiện tại của tài sản
     *
     * @param assetId ID của tài sản
     * @return ResponseEntity chứa giá trị sổ sách
     */
    @GetMapping("/book-value/{assetId}")
    public ResponseEntity<?> getAssetBookValue(@PathVariable Long assetId) {
        try {
            var bookValue = depreciationHistoryService.getCurrentBookValue(assetId);
            Map<String, Object> response = new HashMap<>();
            response.put("assetId", assetId);
            response.put("bookValue", bookValue);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Lỗi: " + e.getMessage()));
        }
    }

    /**
     * GET /api/depreciation/accumulated/{assetId}
     * Lấy tổng khấu hao lũy tích của tài sản
     *
     * @param assetId ID của tài sản
     * @return ResponseEntity chứa tổng khấu hao lũy tích
     */
    @GetMapping("/accumulated/{assetId}")
    public ResponseEntity<?> getTotalAccumulatedDepreciation(@PathVariable Long assetId) {
        try {
            var accumulated = depreciationHistoryService.getTotalAccumulatedDepreciation(assetId);
            Map<String, Object> response = new HashMap<>();
            response.put("assetId", assetId);
            response.put("accumulatedDepreciation", accumulated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Lỗi: " + e.getMessage()));
        }
    }

    /**
     * GET /api/depreciation/year/{year}
     * Lấy tất cả kỳ khấu hao trong một năm
     *
     * @param year Năm (VD: 2024)
     * @return ResponseEntity chứa danh sách khấu hao trong năm
     */
    @GetMapping("/year/{year}")
    public ResponseEntity<?> getDepreciationsByYear(@PathVariable Integer year) {
        try {
            List<DepreciationHistoryDTO> results = depreciationHistoryService.getDepreciationsByYear(year);
            Map<String, Object> response = new HashMap<>();
            response.put("year", year);
            response.put("totalRecords", results.size());
            response.put("data", results);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Lỗi: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/depreciation/{id}
     * Xóa một bản ghi khấu hao
     *
     * @param id ID của bản ghi khấu hao
     * @return ResponseEntity chứa kết quả xóa
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepreciation(@PathVariable Long id) {
        try {
            boolean deleted = depreciationHistoryService.deleteDepreciation(id);
            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Xóa bản ghi khấu hao thành công");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Bản ghi không tồn tại"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Lỗi: " + e.getMessage()));
        }
    }

    // ============ HELPER METHOD ============

    /**
     * Tạo response lỗi chuẩn
     */
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }
}
