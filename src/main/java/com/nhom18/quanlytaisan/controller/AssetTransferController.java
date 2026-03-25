package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.AssetTransferDTO;
import com.nhom18.quanlytaisan.service.AssetTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller cho API quản lý bàn giao/điều chuyển tài sản
 * Base URL: /api/transfers
 */
@RestController
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "*")
public class AssetTransferController {

    @Autowired
    private AssetTransferService assetTransferService;

    /**
     * GET /api/transfers/asset/{assetId}
     * Lấy lịch sử bàn giao/điều chuyển của một tài sản theo Asset ID
     * @param assetId ID của tài sản
     * @return Danh sách lịch sử bàn giao
     */
    @GetMapping("/asset/{assetId}")
    public List<AssetTransferDTO> getTransferHistoryByAssetId(@PathVariable Long assetId) {
        return assetTransferService.getHistoryByAssetId(assetId);
    }

    /**
     * GET /api/transfers/all
     * Lấy tất cả lịch sử bàn giao
     * @return Danh sách tất cả lịch sử bàn giao
     */
    @GetMapping("/all")
    public List<AssetTransferDTO> getAllTransferHistory() {
        return assetTransferService.getAllTransferHistory();
    }

    /**
     * GET /api/transfers/{id}
     * Lấy chi tiết một bản ghi bàn giao theo ID
     * @param id ID của bản ghi bàn giao
     * @return DTO của bản ghi bàn giao
     */
    @GetMapping("/{id}")
    public AssetTransferDTO getTransferById(@PathVariable Long id) {
        return assetTransferService.getTransferById(id);
    }

    /**
     * POST /api/transfers
     * Thực hiện bàn giao/điều chuyển tài sản
     * 
     * Request Body: AssetTransferDTO với các thông tin:
     * {
     *   "assetId": 1,
     *   "fromDepartmentId": 1,
     *   "toDepartmentId": 2,
     *   "fromEmployeeId": 1,
     *   "toEmployeeId": 2,
     *   "transferDate": "2024-01-15",
     *   "reason": "Chuyển vị trí công tác",
     *   "transferBy": "Nguyễn Văn A"
     * }
     * 
     * Response: DTO của bản ghi bàn giao vừa tạo (với ID tự động generate)
     * 
     * @param dto Thông tin bàn giao/điều chuyển
     * @return DTO của bản ghi bàn giao vừa tạo
     */
    @PostMapping
    public AssetTransferDTO transferAsset(@RequestBody AssetTransferDTO dto) {
        return assetTransferService.transferAsset(dto);
    }
}
