-- SQL Migration: Thêm cột tuổi thọ vào bảng assets

-- Bước 1: Thêm cột useful_life_months vào table assets
ALTER TABLE assets ADD COLUMN useful_life_months INTEGER DEFAULT 36;

-- Bước 2: Thêm comment để giải thích
COMMENT ON COLUMN assets.useful_life_months IS 'Tuổi thọ hữu ích của tài sản (tính bằng tháng). Mặc định: 36 tháng (3 năm)';

-- Bước 3: Kiểm tra kết quả
SELECT id, asset_code, name, purchase_price, useful_life_months 
FROM assets 
LIMIT 5;
