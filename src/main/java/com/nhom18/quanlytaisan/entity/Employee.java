package com.nhom18.quanlytaisan.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_employees_department"))
    private Department department;

    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ============ CONSTRUCTORS ============

    /**
     * Constructor không tham số
     */
    public Employee() {
    }

    /**
     * Constructor đầy đủ tham số
     */
    public Employee(Long id, Department department, String employeeCode, String fullName,
                    String jobTitle, LocalDateTime createdAt) {
        this.id = id;
        this.department = department;
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
        this.createdAt = createdAt;
    }

    /**
     * Constructor không có ID (dùng khi INSERT)
     */
    public Employee(Department department, String employeeCode, String fullName, String jobTitle) {
        this.department = department;
        this.employeeCode = employeeCode;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
        this.createdAt = LocalDateTime.now();
    }

    // ============ GETTERS & SETTERS ============

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", department=" + (department != null ? department.getId() : null) +
                ", employeeCode='" + employeeCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
