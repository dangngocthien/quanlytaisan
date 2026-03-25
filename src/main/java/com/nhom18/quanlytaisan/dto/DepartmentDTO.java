package com.nhom18.quanlytaisan.dto;

public class DepartmentDTO {

    private Long id;
    private String code;
    private String name;
    private String description;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public DepartmentDTO() {
    }

    /**
     * Constructor có tham số đầy đủ
     */
    public DepartmentDTO(Long id, String code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor không có ID (dùng khi tạo mới)
     */
    public DepartmentDTO(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    // ============ GETTERS & SETTERS ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
