package com.nhom18.quanlytaisan.scheduler;

import com.nhom18.quanlytaisan.entity.Asset;
import com.nhom18.quanlytaisan.repository.AssetRepository;
import com.nhom18.quanlytaisan.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class MaintenanceScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceScheduler.class);
    private final AssetRepository assetRepository;
    private final NotificationService notificationService;

    public MaintenanceScheduler(AssetRepository assetRepository, NotificationService notificationService) {
        this.assetRepository = assetRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void checkUpcomingMaintenance() {
        logger.info("--- Bắt đầu: Tiến trình kiểm tra bảo trì tài sản chạy ngầm định kỳ ---");

        LocalDate today = LocalDate.now();
        LocalDate next7Days = today.plusDays(7);

        List<Asset> upcomingAssets = assetRepository.findByNextMaintenanceDateBetween(today, next7Days);

        if (upcomingAssets.isEmpty()) {
            logger.info("Không có tài sản nào cần bảo trì trong 7 ngày tới.");
        } else {
            logger.info("Tìm thấy {} tài sản cần bảo trì trong 7 ngày tới.", upcomingAssets.size());
            for (Asset asset : upcomingAssets) {
                long daysLeft = ChronoUnit.DAYS.between(today, asset.getNextMaintenanceDate());
                String alertTitle = "Nhắc bảo trì: " + asset.getName();
                String alertMessage = String.format("Tài sản '%s' (Mã: %s) cần được bảo trì trong %d ngày nữa.", 
                                            asset.getName(), asset.getAssetCode(), daysLeft);
                
                notificationService.createNotification(alertTitle, alertMessage, "/tai-san");
            }
        }

        logger.info("--- Kết thúc tiến trình kiểm tra bảo trì ---");
    }

}
