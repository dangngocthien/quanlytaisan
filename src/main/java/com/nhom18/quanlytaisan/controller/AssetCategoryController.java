package com.nhom18.quanlytaisan.controller;

import com.nhom18.quanlytaisan.dto.AssetCategoryDTO;
import com.nhom18.quanlytaisan.service.AssetCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset-categories")
public class AssetCategoryController {

    private final AssetCategoryService assetCategoryService;

    /**
     * Constructor Dependency Injection
     */
    public AssetCategoryController(AssetCategoryService assetCategoryService) {
        this.assetCategoryService = assetCategoryService;
    }

    /**
     * GET: /api/asset-categories
     * Lấy danh sách tất cả danh mục tài sản
     */
    @GetMapping
    public ResponseEntity<List<AssetCategoryDTO>> getAll() {
        List<AssetCategoryDTO> categories = assetCategoryService.getAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET: /api/asset-categories/{id}
     * Lấy danh mục tài sản theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssetCategoryDTO> getById(@PathVariable Long id) {
        AssetCategoryDTO category = assetCategoryService.getById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * POST: /api/asset-categories
     * Tạo danh mục tài sản mới
     */
    @PostMapping
    public ResponseEntity<AssetCategoryDTO> create(@RequestBody AssetCategoryDTO assetCategoryDTO) {
        AssetCategoryDTO createdCategory = assetCategoryService.create(assetCategoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    /**
     * PUT: /api/asset-categories/{id}
     * Cập nhật danh mục tài sản
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssetCategoryDTO> update(
            @PathVariable Long id,
            @RequestBody AssetCategoryDTO assetCategoryDTO) {
        AssetCategoryDTO updatedCategory = assetCategoryService.update(id, assetCategoryDTO);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * DELETE: /api/asset-categories/{id}
     * Xóa danh mục tài sản
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assetCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET: /api/asset-categories/code/{categoryCode}
     * Tìm danh mục tài sản theo category code
     */
    @GetMapping("/code/{categoryCode}")
    public ResponseEntity<AssetCategoryDTO> findByCategoryCode(@PathVariable String categoryCode) {
        AssetCategoryDTO category = assetCategoryService.findByCategoryCode(categoryCode);
        return ResponseEntity.ok(category);
    }
}
