package com.nhom18.quanlytaisan.config;

import com.nhom18.quanlytaisan.entity.AppUser;
import com.nhom18.quanlytaisan.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Kiểm tra xem hệ thống đã có tài khoản nào chưa
        if (userRepository.count() == 0) {
            // Tạo tài khoản Quản trị viên mặc định
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            // Mật khẩu thiết lập mặc định là "admin123" (đã được băm bằng BCrypt)
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);
            
            userRepository.save(admin);
            System.out.println("----- Đã khởi tạo tài khoản quản trị viên: username=admin / password=admin123 -----");
        }
    }
}
