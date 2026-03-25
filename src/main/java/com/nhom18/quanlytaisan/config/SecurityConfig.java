package com.nhom18.quanlytaisan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cấu hình trung tâm bảo mật (Spring Security Config) sử dụng kiến trúc Component-based hiện đại.
 * 
 * - @EnableWebSecurity: Kích hoạt Web Security
 * - @EnableMethodSecurity: Kích hoạt phân quyền cấp độ hàm (VD: @PreAuthorize("hasRole('ADMIN')"))
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    // Constructor Injection
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Thuật toán băm mật khẩu chuẩn công nghiệp (BCrypt)
     * Ngăn chặn các cuộc tấn công Brute-force & Rainbow table
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Strength (log iterations): 10 là mức cân bằng giữa bảo mật và hiệu năng
    }

    /**
     * Expose AuthenticationManager ra ngoài để sử dụng ở các lớp Controller/Service nếu cần custom login API
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Bức tường lửa (Firewall) chính của hệ thống.
     * Quy định phân quyền, URL truy cập và cơ chế CSRF / CORS.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Tạm thời vô hiệu hóa bộ lọc CSRF đối với API POST (Lưu ý: trên Production nên bật lại và dùng Token)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) 

            // Cấu hình danh sách trắng (Whitelist) và bộ kiểm soát URL
            .authorizeHttpRequests(auth -> auth
                // 1. Static Resources: Cho phép mọi request mà không cần kiểm tra quyền
                .requestMatchers(
                    "/css/**", 
                    "/js/**", 
                    "/images/**", 
                    "/webjars/**", 
                    "/favicon.ico"
                ).permitAll()
                
                // 2. Authentication Endpoints: Cho phép truy cập công khai
                .requestMatchers("/login", "/error").permitAll()
                
                // 3. Optional - Business Endpoints (REST API) 
                // Nếu muốn khóa API thì đóng dòng dưới lại
                // .requestMatchers("/api/**").hasAnyRole("ADMIN", "USER") 
                
                // 4. Default Rule: Bất kì request nào không liệt kê ở trên đều bắt buộc phải đăng nhập (Authentication)
                .anyRequest().authenticated()
            )

            // Cấu hình cơ chế Form Login truyền thống (Session-based)
            .formLogin(form -> form
                .loginPage("/login")                                // Endpoint view template
                .loginProcessingUrl("/login")                       // Endpoint POST submit thông tin
                .defaultSuccessUrl("/", true)               // Force redirect về trang chủ khi login thành công
                .failureUrl("/login?error=true")                    // Trả về error state
                .permitAll()
            )

            // Cấu hình cơ chế Logout (Xoá Cookie & Invalid Session)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
