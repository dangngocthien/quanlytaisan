package com.nhom18.quanlytaisan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_categories")
public class AssetCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_code", nullable = false, unique = true, length = 50)
    private String categoryCode;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "default_useful_life_months", nullable = false)
    private Integer defaultUsefulLifeMonths;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public AssetCategory() {
    }

    /**
     * Constructor đầy đủ tham số
     */
    public AssetCategory(Long id, String categoryCode, String name, Integer defaultUsefulLifeMonths,
                         String description, LocalDateTime createdAt) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.name = name;
        this.defaultUsefulLifeMonths = defaultUsefulLifeMonths;
        this.description = description;
        this.createdAt = createdAt;
    }

    /**
     * Constructor không có ID (dùng khi INSERT)
     */
    public AssetCategory(String categoryCode, String name, Integer defaultUsefulLifeMonths, String description) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.defaultUsefulLifeMonths = defaultUsefulLifeMonths;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    // ============ GETTERS & SETTERS ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDefaultUsefulLifeMonths() {
        return defaultUsefulLifeMonths;
    }

    public void setDefaultUsefulLifeMonths(Integer defaultUsefulLifeMonths) {
        this.defaultUsefulLifeMonths = defaultUsefulLifeMonths;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AssetCategory{" +
                "id=" + id +
                ", categoryCode='" + categoryCode + '\'' +
                ", name='" + name + '\'' +
                ", defaultUsefulLifeMonths=" + defaultUsefulLifeMonths +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
