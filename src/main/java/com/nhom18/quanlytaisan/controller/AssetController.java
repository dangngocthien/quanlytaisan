package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.AssetDTO;
import com.nhom18.quanlytaisan.service.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    /**
     * Constructor Dependency Injection
     */
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * GET: /api/assets
     * Lấy danh sách tất cả tài sản
     */
    @GetMapping
    public ResponseEntity<List<AssetDTO>> getAll() {
        List<AssetDTO> assets = assetService.getAll();
        return ResponseEntity.ok(assets);
    }

    /**
     * GET: /api/assets/{id}
     * Lấy tài sản theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getById(@PathVariable Long id) {
        AssetDTO asset = assetService.getById(id);
        return ResponseEntity.ok(asset);
    }

    /**
     * POST: /api/assets
     * Tạo tài sản mới
     */
    @PostMapping
    public ResponseEntity<AssetDTO> create(@RequestBody AssetDTO assetDTO) {
        AssetDTO createdAsset = assetService.create(assetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }

    /**
     * PUT: /api/assets/{id}
     * Cập nhật tài sản
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> update(
            @PathVariable Long id,
            @RequestBody AssetDTO assetDTO) {
        AssetDTO updatedAsset = assetService.update(id, assetDTO);
        return ResponseEntity.ok(updatedAsset);
    }

    /**
     * DELETE: /api/assets/{id}
     * Xóa tài sản
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET: /api/assets/code/{assetCode}
     * Tìm tài sản theo asset code
     */
    @GetMapping("/code/{assetCode}")
    public ResponseEntity<AssetDTO> findByAssetCode(@PathVariable String assetCode) {
        AssetDTO asset = assetService.findByAssetCode(assetCode);
        return ResponseEntity.ok(asset);
    }

    /**
     * GET: /api/assets/category/{categoryId}
     * Lấy danh sách tài sản theo category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<AssetDTO>> findByCategoryId(@PathVariable Long categoryId) {
        List<AssetDTO> assets = assetService.findByCategoryId(categoryId);
        return ResponseEntity.ok(assets);
    }

    /**
     * GET: /api/assets/department/{departmentId}
     * Lấy danh sách tài sản theo phòng ban
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<AssetDTO>> findByCurrentDepartmentId(@PathVariable Long departmentId) {
        List<AssetDTO> assets = assetService.findByCurrentDepartmentId(departmentId);
        return ResponseEntity.ok(assets);
    }

    /**
     * GET: /api/assets/status/{status}
     * Lấy danh sách tài sản theo trạng thái
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AssetDTO>> findByStatus(@PathVariable String status) {
        List<AssetDTO> assets = assetService.findByStatus(status);
        return ResponseEntity.ok(assets);
    }
}


