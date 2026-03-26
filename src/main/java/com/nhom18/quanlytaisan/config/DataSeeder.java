package com.nhom18.quanlytaisan.config;

import com.nhom18.quanlytaisan.entity.AppUser;
import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.entity.WarrantyRecord;
import com.nhom18.quanlytaisan.repository.AppUserRepository;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import com.nhom18.quanlytaisan.repository.WarrantyRecordRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssetRepository assetRepository;
    private final WarrantyRecordRepository warrantyRecordRepository;

    public DataSeeder(AppUserRepository userRepository, PasswordEncoder passwordEncoder,
                      AssetRepository assetRepository, WarrantyRecordRepository warrantyRecordRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.assetRepository = assetRepository;
        this.warrantyRecordRepository = warrantyRecordRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Khởi tạo tài khoản
        if (userRepository.count() == 0) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("----- Đã khởi tạo tài khoản quản trị viên: username=admin / password=admin123 -----");
        }

        // Tự động thêm hồ sơ bảo hành mẫu với các ảnh trong folder Pictures
        if (warrantyRecordRepository.count() == 0) {
            List<Asset> assets = assetRepository.findAll();
            if (!assets.isEmpty()) {
                Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                String[] samplePictures = {
                        "bienlai1.jpg", "bienlai2.jpg", "bienlai3.jpg",
                        "bienlai4.jpg", "bienlai5.jpg", "bienlai6.jpg"
                };
                
                String[] providers = {
                        "Công ty FPT Shop", "Cửa hàng Phong Vũ", "Điện Máy Xanh",
                        "Tiki Trading", "Thế Giới Di Động", "Trần Anh"
                };

                int limit = Math.min(6, assets.size());
                
                for (int i = 0; i < limit; i++) {
                    Asset asset = assets.get(i);
                    String picName = samplePictures[i];
                    Path sourcePath = Paths.get("Pictures", picName).toAbsolutePath();
                    
                    if (Files.exists(sourcePath)) {
                        String fileExtension = ".jpg";
                        String fileNameToStore = UUID.randomUUID().toString() + fileExtension;
                        Path targetPath = uploadDir.resolve(fileNameToStore);
                        
                        try {
                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                            
                            WarrantyRecord record = new WarrantyRecord();
                            record.setAsset(asset);
                            record.setProviderCompany(providers[i]);
                            record.setContactPhone("090" + (1000000 + (int)(Math.random() * 8999999)));
                            record.setStartDate(LocalDate.now().minusMonths(i * 2L));
                            record.setEndDate(LocalDate.now().plusYears(1).minusMonths(i * 2L));
                            record.setNotes("Phiếu bảo hành gốc đính kèm.");
                            record.setAttachmentFileName(picName);
                            record.setAttachmentPath(fileNameToStore);
                            record.setAttachmentType("image/jpeg");
                            
                            warrantyRecordRepository.save(record);
                            System.out.println("----- Đã tạo hồ sơ bảo hành cho tài sản " + asset.getAssetCode() + " với ảnh " + picName + " -----");
                        } catch (IOException e) {
                            System.err.println("Không thể copy file ảnh: " + e.getMessage());
                        }
                    } else {
                        System.err.println("Không tìm thấy file mẫu ở vị trí: " + sourcePath.toString());
                    }
                }
            }
        }
    }
}
