package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.AssetTransferDTO;
import java.util.List;

/**
 * Service interface cho quản lý bàn giao/điều chuyển tài sản
 */
public interface AssetTransferService {

    /**
     * Lấy lịch sử bàn giao/điều chuyển của một tài sản theo Asset ID
     * @param assetId ID của tài sản
     * @return Danh sách lịch sử bàn giao (DTO)
     */
    List<AssetTransferDTO> getHistoryByAssetId(Long assetId);

    /**
     * Thực hiện bàn giao/điều chuyển tài sản
     * Hàm này là TRANSACTIONAL - thực hiện 2 bước trong 1 transaction:
     *   Bước 1: Lưu thông tin bàn giao vào bảng AssetTransfer
     *   Bước 2: Cập nhật currentDepartmentId và currentEmployeeId của tài sản
     * @param dto Thông tin bàn giao/điều chuyển
     * @return DTO của bản ghi bàn giao vừa tạo
     */
    AssetTransferDTO transferAsset(AssetTransferDTO dto);

    /**
     * Lấy tất cả lịch sử bàn giao
     * @return Danh sách tất cả lịch sử bàn giao
     */
    List<AssetTransferDTO> getAllTransferHistory();

    /**
     * Lấy chi tiết một bản ghi bàn giao theo ID
     * @param id ID của bản ghi bàn giao
     * @return DTO của bản ghi bàn giao
     */
    AssetTransferDTO getTransferById(Long id);
}
