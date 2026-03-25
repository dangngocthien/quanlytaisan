package com.nhom18.quanlytaisan.dto;

public class AssetCategoryDTO {

    private Long id;
    private String categoryCode;
    private String name;
    private Integer defaultUsefulLifeMonths;
    private String description;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public AssetCategoryDTO() {
    }

    /**
     * Constructor có tham số đầy đủ
     */
    public AssetCategoryDTO(Long id, String categoryCode, String name, Integer defaultUsefulLifeMonths,
                           String description) {
        this.id = id;
        this.categoryCode = categoryCode;
        this.name = name;
        this.defaultUsefulLifeMonths = defaultUsefulLifeMonths;
        this.description = description;
    }

    /**
     * Constructor không có ID (dùng khi tạo mới)
     */
    public AssetCategoryDTO(String categoryCode, String name, Integer defaultUsefulLifeMonths,
                           String description) {
        this.categoryCode = categoryCode;
        this.name = name;
        this.defaultUsefulLifeMonths = defaultUsefulLifeMonths;
        this.description = description;
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

    @Override
    public String toString() {
        return "AssetCategoryDTO{" +
                "id=" + id +
                ", categoryCode='" + categoryCode + '\'' +
                ", name='" + name + '\'' +
                ", defaultUsefulLifeMonths=" + defaultUsefulLifeMonths +
                ", description='" + description + '\'' +
                '}';
    }
}
