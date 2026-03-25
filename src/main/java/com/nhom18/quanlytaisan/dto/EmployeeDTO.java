package com.nhom18.quanlytaisan.dto;

public class EmployeeDTO {

    private Long id;
    private String employeeCode;
    private String fullName;
    private String jobTitle;
    private Long departmentId;
    private String departmentName;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public EmployeeDTO() {
    }

    /**
     * Constructor có tham số đầy đủ
     */
    public EmployeeDTO(Long id, String employeeCode, String fullName, String jobTitle,
                      Long departmentId, String departmentName) {
        this.id = id;
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    /**
     * Constructor không có ID (dùng khi tạo mới)
     */
    public EmployeeDTO(String employeeCode, String fullName, String jobTitle,
                      Long departmentId, String departmentName) {
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    // ============ GETTERS & SETTERS ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "id=" + id +
                ", employeeCode='" + employeeCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
