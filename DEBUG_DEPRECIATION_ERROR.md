# 🔧 Hướng Dẫn Sửa Lỗi Dữ Liệu Khấu Hao

## Vấn Đề

- Tài sản **TS-PC-001** có giá mua = 12.000.000 VNĐ
- Nhưng cột "Giá trị còn lại" hiển thị 14.583.333 (cao hơn giá mua, **không hợp lệ**)

## Root Cause Analysis

### Logic Khấu Hao (Trong Code) - ✅ ĐÚNG

```
Khấu hao tháng = Giá mua / 36 tháng
              = 12.000.000 / 36
              = 333.333 VNĐ/tháng

Giá trị còn lại = Giá mua - Khấu hao lũy tích
               ≤ 12.000.000 (không thể vượt quá)
```

### Tại Sao Dữ Liệu Sai?

- Bản ghi trong table `depreciation_history` có `remaining_value = 14.583.333`
- Điều này không thể xảy ra do logic code
- **Nguyên nhân**: Dữ liệu này được **manual nhập sai** hoặc **import từ file test**

---

## 📋 Cách Sửa (3 Bước)

### **Bước 1: Xóa Dữ Liệu Sai**

Mở **DBeaver**, **pgAdmin**, hoặc terminal PostgreSQL và chạy:

```sql
-- Xóa tất cả bản ghi khấu hao của TS-PC-001
DELETE FROM depreciation_history
WHERE asset_id = (SELECT id FROM assets WHERE asset_code = 'TS-PC-001');
```

**Hoặc nếu bạn biết asset_id**:

```sql
-- Ví dụ: nếu asset_id = 5
DELETE FROM depreciation_history WHERE asset_id = 5;
```

**Kiểm tra**:

```sql
SELECT COUNT(*) FROM depreciation_history
WHERE asset_id = (SELECT id FROM assets WHERE asset_code = 'TS-PC-001');
-- Kết quả phải là: 0
```

---

### **Bước 2: Tính Lại Khấu Hao Qua API**

Chọn **1 trong 2 cách**:

#### **Cách A: Tính cho tháng cụ thể**

```bash
POST http://localhost:8080/quanlytaisan/api/depreciation/calculate?assetId=<ID_TS-PC-001>&month=3&year=2024
```

**Ví dụ cURL**:

```bash
curl -X POST "http://localhost:8080/quanlytaisan/api/depreciation/calculate?assetId=5&month=3&year=2024"
```

#### **Cách B: Tính cho tất cả tài sản trong 1 tháng**

```bash
POST http://localhost:8080/quanlytaisan/api/depreciation/calculate-all?month=3&year=2024
```

---

### **Bước 3: Xác Minh Dữ Liệu**

**Kiểm tra dữ liệu đã được cập nhật**:

```bash
# Lấy lịch sử khấu hao của TS-PC-001
GET http://localhost:8080/quanlytaisan/api/depreciation/history/<asset_id>
```

**Kết quả dự kiến**:

```json
{
  "assetId": 5,
  "assetCode": "TS-PC-001",
  "depreciationAmount": 333333.33,
  "remainingValue": 11666666.67, // ← Phải nhỏ hơn 12.000.000
  "purchasePrice": 12000000.0
}
```

---

## 🛡️ Phòng Chống Lỗi Tương Tự

### Kiểm Tra Định Kỳ

Chạy SQL này mỗi tháng để phát hiện dữ liệu sai:

```sql
-- Tìm các bản ghi khấu hao bất thường
-- (remaining_value > purchase_price)
SELECT dh.id, a.asset_code, dh.remaining_value, a.purchase_price
FROM depreciation_history dh
JOIN assets a ON dh.asset_id = a.id
WHERE dh.remaining_value > a.purchase_price;

-- Kết quả phải là: empty (không có bản ghi nào)
```

### Thêm Validation Vào Code

Trong `DepreciationHistoryServiceImpl.java`, thêm kiểm tra:

```java
// Kiểm tra remaining_value không vượt quá purchase_price
if (remainingValue.compareTo(asset.getPurchasePrice()) > 0) {
    throw new RuntimeException("❌ Lỗi: Giá trị sổ sách không thể vượt quá giá mua!");
}
```

---

## 📞 Nếu Vấn Đề Vẫn Tồn Tại

1. **Kiểm tra toàn bộ bản ghi**:

   ```sql
   SELECT * FROM depreciation_history WHERE asset_id = <ID>;
   ```

2. **Xóa tất cả và tính lại từ đầu**:

   ```sql
   DELETE FROM depreciation_history;
   ```

   Sau đó chọn một tháng và tính lại cho tất cả tài sản.

3. **Liên hệ hỗ trợ** với thông tin:
   - Asset Code: TS-PC-001
   - Purchase Price: 12.000.000
   - Expected remaining value: ≤ 12.000.000
   - Actual remaining value: 14.583.333
