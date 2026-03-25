package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.AssetTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository cho Entity AssetTransfer
 */
@Repository
public interface AssetTransferRepository extends JpaRepository<AssetTransfer, Long> {

    /**
     * Tìm kiếm lịch sử bàn giao/điều chuyển tài sản theo Asset ID
     * @param assetId ID của tài sản
     * @return Danh sách lịch sử bàn giao
     */
    List<AssetTransfer> findByAsset_Id(Long assetId);

    /**
     * Tìm kiếm lịch sử bàn giao/điều chuyển tài sản theo Department From
     * @param departmentId ID của phòng ban nguồn
     * @return Danh sách lịch sử bàn giao
     */
    List<AssetTransfer> findByFromDepartment_Id(Long departmentId);

    /**
     * Tìm kiếm lịch sử bàn giao/điều chuyển tài sản theo Department To
     * @param departmentId ID của phòng ban đích
     * @return Danh sách lịch sử bàn giao
     */
    List<AssetTransfer> findByToDepartment_Id(Long departmentId);
}
