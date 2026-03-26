-- Cập nhật bảng assets để thêm các cột quản lý lịch bảo trì
ALTER TABLE assets 
ADD COLUMN maintenance_cycle INTEGER,
ADD COLUMN last_maintenance_date DATE,
ADD COLUMN next_maintenance_date DATE;

-- Khởi tạo giá trị mặc định cho các tài sản đã có:
-- Chu kỳ bảo trì mặc định là 6 tháng
-- Ngày bảo trì lần cuối là ngày mua (purchase_date)
-- Ngày bảo trì tiếp theo là ngày mua + 6 tháng
UPDATE assets
SET 
    maintenance_cycle = 6,
    last_maintenance_date = purchase_date,
    next_maintenance_date = purchase_date + interval '6 months'
WHERE maintenance_cycle IS NULL AND purchase_date IS NOT NULL;
