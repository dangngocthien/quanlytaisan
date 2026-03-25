package com.nhom18.quanlytaisan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Trả về template login.html trong thư mục resources/templates
    }

    @GetMapping("/")
    public String homePage() {
        return "redirect:/baocao/department-dashboard"; // Chuyển hướng sang trang Dashboard
    }
}
