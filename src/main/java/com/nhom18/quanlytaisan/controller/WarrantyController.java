package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.WarrantyRecordDTO;
import com.nhom18.quanlytaisan.dto.WarrantyRequestDTO;
import com.nhom18.quanlytaisan.service.FileStorageService;
import com.nhom18.quanlytaisan.service.WarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WarrantyController {

    private final WarrantyService warrantyService;
    private final FileStorageService fileStorageService;

    @Autowired
    public WarrantyController(WarrantyService warrantyService, FileStorageService fileStorageService) {
        this.warrantyService = warrantyService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/assets/{assetId}/warranties")
    public ResponseEntity<List<WarrantyRecordDTO>> getWarrantiesByAssetId(@PathVariable Long assetId) {
        List<WarrantyRecordDTO> warranties = warrantyService.getWarrantiesByAssetId(assetId);
        return ResponseEntity.ok(warranties);
    }

    @PostMapping(value = "/assets/{assetId}/warranties", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createWarranty(
            @PathVariable Long assetId,
            @ModelAttribute WarrantyRequestDTO requestDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            WarrantyRecordDTO createdWarranty = warrantyService.createWarranty(assetId, requestDTO, file);
            return new ResponseEntity<>(createdWarranty, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể lưu hồ sơ bảo hành: " + e.getMessage());
        }
    }

    @DeleteMapping("/warranties/{warrantyId}")
    public ResponseEntity<?> deleteWarranty(@PathVariable Long warrantyId) {
        try {
            warrantyService.deleteWarranty(warrantyId);
            return ResponseEntity.ok("Xóa hồ sơ bảo hành thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xóa: " + e.getMessage());
        }
    }

    @GetMapping("/warranties/files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Ignored
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}