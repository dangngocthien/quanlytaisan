package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.WarrantyRecordDTO;
import com.nhom18.quanlytaisan.dto.WarrantyRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WarrantyService {
    List<WarrantyRecordDTO> getWarrantiesByAssetId(Long assetId);
    WarrantyRecordDTO createWarranty(Long assetId, WarrantyRequestDTO requestDTO, MultipartFile file);
    void deleteWarranty(Long warrantyId);
}