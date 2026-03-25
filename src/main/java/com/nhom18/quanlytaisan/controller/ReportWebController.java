package com.nhom18.quanlytaisan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Report Web Controller
 * Pure routing controller for Thymeleaf templates
 * 
 * @author Senior Spring Boot Developer
 */
@Controller
@RequestMapping("/baocao")
public class ReportWebController {
    
    /**
     * Display Depreciation Report Page
     */
    @GetMapping("/depreciation")
    public String getDepreciationReport() {
        return "baocao/depreciation-report";
    }

    /**
     * Display Valuation Report Page
     */
    @GetMapping("/valuation")
    public String getValuationReport() {
        return "baocao/valuation-report";
    }

    /**
     * Display Department Dashboard Page
     */
    @GetMapping("/department-dashboard")
    public String getDepartmentDashboard() {
        return "baocao/department-dashboard";
    }
}
