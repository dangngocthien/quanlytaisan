package com.nhom18.quanlytaisan.repository;

import com.nhom18.quanlytaisan.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    
    /**
     * Tìm kiếm người dùng dựa trên tên đăng nhập (username)
     * Spring Data JPA tự động sinh câu SQL (SELECT * FROM app_users WHERE username = ?)
     */
    Optional<AppUser> findByUsername(String username);
    
    /**
     * Kiểm tra xem username đã tồn tại chưa
     */
    boolean existsByUsername(String username);
}
