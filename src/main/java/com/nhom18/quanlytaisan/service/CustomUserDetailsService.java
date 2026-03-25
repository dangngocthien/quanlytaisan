package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.entity.AppUser;
import com.nhom18.quanlytaisan.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service xử lý logic xác thực người dùng dựa trên thông tin lưu trong Database.
 * Core component của Spring Security Authentication Flow.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    // Constructor Injection là best practice thay vì dùng @Autowired trên field
    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm User trong DB
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với tên đăng nhập: " + username));

        // Chuẩn hóa ROLE (Spring Security yêu cầu format phân quyền cực kỳ nghiêm ngặt)
        // Nếu role trong DB là "ROLE_ADMIN", ta phải bỏ chữ "ROLE_" vì hàm roles() tự động thêm vào
        String roleName = appUser.getRole();
        if (roleName != null && roleName.startsWith("ROLE_")) {
            roleName = roleName.substring(5);
        }

        // Xây dựng đối tượng UserDetails (chuẩn của Spring Security)
        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(roleName)
                .disabled(!appUser.isEnabled())
                // Optional: Có thể cấu hình thêm accountExpired, accountLocked, credentialsExpired nếu cần
                .build();
    }
}
