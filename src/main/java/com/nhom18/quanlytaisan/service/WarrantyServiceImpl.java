package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.dto.WarrantyRecordDTO;
import com.nhom18.quanlytaisan.dto.WarrantyRequestDTO;
import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.entity.WarrantyRecord;
import com.nhom18.quanlytaisan.exception.ResourceNotFoundException;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import com.nhom18.quanlytaisan.repository.WarrantyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WarrantyServiceImpl implements WarrantyService {

    private final WarrantyRecordRepository warrantyRepository;
    private final AssetRepository assetRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public WarrantyServiceImpl(WarrantyRecordRepository warrantyRepository, 
                               AssetRepository assetRepository, 
                               FileStorageService fileStorageService) {
        this.warrantyRepository = warrantyRepository;
        this.assetRepository = assetRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<WarrantyRecordDTO> getWarrantiesByAssetId(Long assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new ResourceNotFoundException("Không tìm thấy tài sản với ID: " + assetId);
        }
        List<WarrantyRecord> records = warrantyRepository.findByAssetIdOrderByCreatedAtDesc(assetId);
        return records.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WarrantyRecordDTO createWarranty(Long assetId, WarrantyRequestDTO requestDTO, MultipartFile file) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài sản với ID: " + assetId));
                
        if ("THANH_LY".equalsIgnoreCase(asset.getStatus())) {
            throw new RuntimeException("Tài sản đã thanh lý, không thể cập nhật thêm hồ sơ bảo hành!");
        }

        // Debug: Log dữ liệu DTO
        System.out.println("WarrantyService: providerCompany = " + requestDTO.getProviderCompany());
        System.out.println("WarrantyService: contactPhone = " + requestDTO.getContactPhone());
        System.out.println("WarrantyService: startDate = " + requestDTO.getStartDate());
        System.out.println("WarrantyService: endDate = " + requestDTO.getEndDate());

        WarrantyRecord record = new WarrantyRecord();
        record.setAsset(asset);
        record.setProviderCompany(requestDTO.getProviderCompany());
        record.setContactPhone(requestDTO.getContactPhone());
        record.setStartDate(requestDTO.getStartDate());
        record.setEndDate(requestDTO.getEndDate());
        record.setNotes(requestDTO.getNotes());

        if (file != null && !file.isEmpty()) {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "document");
            String storedFileName = fileStorageService.storeFile(file);
            
            record.setAttachmentFileName(originalFileName);
            record.setAttachmentPath(storedFileName);
            record.setAttachmentType(file.getContentType());
        }

        WarrantyRecord savedRecord = warrantyRepository.save(record);
        return mapToDTO(savedRecord);
    }

    @Override
    @Transactional
    public void deleteWarranty(Long warrantyId) {
        WarrantyRecord record = warrantyRepository.findById(warrantyId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hồ sơ bảo hành với ID: " + warrantyId));

        if (StringUtils.hasText(record.getAttachmentPath())) {
            fileStorageService.deleteFile(record.getAttachmentPath());
        }
        warrantyRepository.delete(record);
    }

    private WarrantyRecordDTO mapToDTO(WarrantyRecord entity) {
        WarrantyRecordDTO dto = new WarrantyRecordDTO();
        dto.setId(entity.getId());
        dto.setAssetId(entity.getAsset().getId());
        dto.setAssetCode(entity.getAsset().getAssetCode());
        dto.setAssetName(entity.getAsset().getName());
        dto.setProviderCompany(entity.getProviderCompany());
        dto.setContactPhone(entity.getContactPhone());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setNotes(entity.getNotes());
        dto.setAttachmentFileName(entity.getAttachmentFileName());
        dto.setAttachmentType(entity.getAttachmentType());
        dto.setCreatedAt(entity.getCreatedAt());

        if (StringUtils.hasText(entity.getAttachmentPath())) {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/warranties/files/")
                    .path(entity.getAttachmentPath())
                    .toUriString();
            dto.setFileDownloadUrl(fileDownloadUri);
        }

        return dto;
    }
}