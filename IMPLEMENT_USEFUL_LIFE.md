# 🔧 Hướng Dẫn Thêm Cột Tuổi Thọ (Useful Life) Vào Database

## 📋 Thay Đổi Được Thực Hiện

### Code Changes:

1. ✅ **Entity Asset**: Thêm trường `usefulLifeMonths` (mặc định 36 tháng)
2. ✅ **DTO AssetDTO**: Thêm trường `usefulLifeMonths`
3. ✅ **Service DepreciationHistoryServiceImpl**: Cập nhật để sử dụng `asset.getUsefulLifeMonths()` thay vì constant cứng
4. ✅ **Interface DepreciationHistoryService**: Cập nhật documentation

### Database Changes:

Thêm cột mới `useful_life_months` vào table `assets`

---

## 🚀 Cách Triển Khai

### **Bước 1: Thêm Cột Vào Database**

Chạy SQL script sau (dùng DBeaver, pgAdmin, hoặc psql):

```sql
-- Thêm cột tuổi thọ vào table assets
ALTER TABLE assets ADD COLUMN useful_life_months INTEGER DEFAULT 36;

-- Thêm comment để giải thích
COMMENT ON COLUMN assets.useful_life_months IS 'Tuổi thọ hữu ích của tài sản (tính bằng tháng). Mặc định: 36 tháng (3 năm)';

-- Kiểm tra kết quả
SELECT id, asset_code, name, useful_life_months
FROM assets
LIMIT 5;
```

**Kết quả dự kiến**: Tất cả tài sản hiện có sẽ có `useful_life_months = 36`

---

### **Bước 2: Rebuild Project (Maven)**

```bash
cd g:\My Drive\LAP TRINH JAVA\quanlytaisan
mvn clean install
```

Hoặc nếu dùng IDE, nhấn `Ctrl+Shift+F10` (IntelliJ) hoặc `Ctrl+B` (VS Code)

---

### **Bước 3: Restart Application**

1. Stop ứng dụng hiện tại (nếu đang chạy)
2. Start lại ứng dụng
3. Hibernate sẽ automatically detect column (vì `spring.jpa.hibernate.ddl-auto=update`)

---

### **Bước 4: Test Chức Năng**

#### **A. Cập nhật tuổi thọ cho 1 tài sản (SQL)**

```sql
-- Ví dụ: tài sản TS-PC-001 có tuổi thọ 60 tháng (5 năm)
UPDATE assets
SET useful_life_months = 60
WHERE asset_code = 'TS-PC-001';

-- Kiểm tra
SELECT asset_code, name, useful_life_months FROM assets WHERE asset_code = 'TS-PC-001';
```

#### **B. Tính lại khấu hao**

```bash
# API tính khấu hao cho tài sản cụ thể
POST http://localhost:8080/quanlytaisan/api/depreciation/calculate?assetId=<ID>&month=3&year=2024
```

**Response dự kiến** (với tuổi thọ 60 tháng):

```json
{
  "depreciationAmount": 200000, // 12.000.000 / 60 = 200.000
  "remainingValue": 11800000 // 12.000.000 - 200.000
}
```

So sánh với tuổi thọ 36 tháng (cũ):

```
khấu hao/tháng = 12.000.000 / 36 = 333.333
```

---

## 📊 Bảng So Sánh

| Tuổi Thọ  | Khấu Hao/Tháng | Sau 12 Tháng | Sau 36 Tháng |
| --------- | -------------- | ------------ | ------------ |
| 36 tháng  | 333,333 VNĐ    | 11,000,000   | 0            |
| 60 tháng  | 200,000 VNĐ    | 9,600,000    | 3,200,000    |
| 120 tháng | 100,000 VNĐ    | 8,800,000    | 8,800,000    |

---

## ⚠️ Lưu Ý Quan Trọng

1. **Dữ liệu lịch sử**: Khấu hao đã tính trước đó sẽ **không** tự động cập nhật
   - Phải xóa bản ghi cũ và tính lại

   ```sql
   DELETE FROM depreciation_history;
   ```

2. **Default Value**: Nếu `useful_life_months` = NULL, code sẽ tự động fallback về 36 tháng

3. **Validation**: Kiểm tra tính hợp lệ:
   ```sql
   -- Tìm tài sản có tuổi thọ không hợp lệ
   SELECT id, asset_code, useful_life_months
   FROM assets
   WHERE useful_life_months <= 0 OR useful_life_months IS NULL;
   ```

---

## 🔄 Rollback (Nếu Cần)

Nếu cần rollback các thay đổi:

```sql
-- Xóa cột khỏi database
ALTER TABLE assets DROP COLUMN useful_life_months;
```

---

## ✅ Checklist

- [ ] Chạy SQL script thêm cột
- [ ] Rebuild project (Maven clean install)
- [ ] Restart application
- [ ] Test API tính khấu hao
- [ ] Xác minh khấu hao được tính đúng với tuổi thọ mới
