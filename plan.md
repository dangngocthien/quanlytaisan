# 📋 ROADMAP QUẢN LÝ TÀI SẢN - Chi tiết triển khai

Dưới đây là kế hoạch triển khai **5 giai đoạn** với các mục tiêu cụ thể, ưu tiên tính năng cốt lõi:

---

## **PHASE 1: Thiết kế & Cơ sở hạ tầng** (Tuần 1-2)

**Mục tiêu:** Xây dựng nền móng vững chắc

### 1.1 Thiết kế cơ sở dữ liệu (Database Schema)

- **Bảng chính:**
  - `DanhMucTaiSan` (asset categories)
  - `TaiSan` (assets - mã, tên, loại, giá trị, ngày mua, trạng thái)
  - `PhongBan` (departments)
  - `NhanVien` (employees)
  - `PhanBo` (asset allocation - who has which asset)
  - `LichSuDieuChuyen` (asset movement history)
  - `KhauHao` (depreciation records)

### 1.2 Chuẩn bị project structure

- Tạo cấu trúc folding theo **Clean Architecture**
- Định cấu hình `pom.xml`: Spring Boot Web, JPA, SQL Server Driver, Thymeleaf, Bootstrap
- Setup `application.properties`: database connection, JPA config

---

## **PHASE 2: Backend cơ bản (CRUD Tài sản)** (Tuần 3-4)

**Mục tiêu:** Hoàn thiện phần core quản lý tài sản

### 2.1 Entity & JPA

- ✅ `DanhMucTaiSan`, `TaiSan`, `PhongBan`, `NhanVien` entities
- Định nghĩa relationships (@OneToMany, @ManyToOne)
- Thêm auditing fields (createdDate, modifiedDate)

### 2.2 DTO Layer

- `TaiSanDTO`, `DanhMucDTO`, `PhongBanDTO` cho request/response

### 2.3 Repository & Service

- `TaiSanRepository`, `DanhMucRepository` (JPA Repository)
- `TaiSanService`: CRUD + tìm kiếm theo mã/loại/phòng ban

### 2.4 Controller & API

- `TaiSanController`: GET/POST/PUT/DELETE endpoints

---

## **PHASE 3: Quản lý phân bổ & trạng thái** (Tuần 5-6)

**Mục tiêu:** Xử lý phân bổ tài sản và lịch sử thay đổi

### 3.1 Entity & Service mở rộng

- `PhanBo` entity với FK đến TaiSan, NhanVien, PhongBan
- `LichSuDieuChuyen` entity (ghi nhận mọi thay đổi: điều chuyển, bảo trì, thanh lý)
- Service xử lý: phân bổ → phòng ban/nhân viên

### 3.2 Trạng thái tài sản

- Enum: `DANG_DUNG`, `BAO_TRI`, `THANH_LY`
- Cập nhật trạng thái → tự động ghi vào `LichSuDieuChuyen`

### 3.3 Controller & Business Logic

- Endpoints: phân bổ, lấy lịch sử, thay đổi trạng thái

---

## **PHASE 4: Tính khấu hao & Báo cáo** (Tuần 7-8)

**Mục tiêu:** Tính toán giá trị còn lại và báo cáo

### 4.1 KhauHao Entity & Service

- Tính khấu hao theo chu kỳ (hàng tháng/hàng quý)
- Công thức: Giá trị còn lại = Giá gốc - Khấu hao lũy kế

### 4.2 Service báo cáo

- Lấy danh sách tài sản với giá trị còn lại
- Báo cáo theo phòng ban
- Báo cáo tài sản theo loại

### 4.3 Controller

- Endpoints báo cáo dữ liệu (JSON)

---

## **PHASE 5: Frontend UI (Thymeleaf + Bootstrap 5)** (Tuần 9-11)

**Mục tiêu:** Giao diện người dùng hoàn chỉnh cho tính năng cốt lõi

### 5.1 Layout & Navigation

- Master layout (header, sidebar, footer)
- Menu: Quản lý tài sản, Phân bổ, Tra cứu, Báo cáo

### 5.2 Màn hình chính

| Màn hình                | Chức năng                                    |
| ----------------------- | -------------------------------------------- |
| **Danh sách tài sản**   | CRUD, tìm kiếm, filter theo loại/phòng ban   |
| **Thêm/Sửa tài sản**    | Form ghi nhập mua sắm, giá trị, ngày sử dụng |
| **Phân bổ tài sản**     | Assign tài sản → nhân viên/phòng ban         |
| **Lịch sử điều chuyển** | Xem timeline thay đổi trạng thái             |
| **Báo cáo**             | Hiển thị danh sách tài sản + giá trị còn lại |
| **Tra cứu**             | Search nhanh theo mã/tên                     |

### 5.3 Công nghệ frontend

- Thymeleaf templates + Bootstrap 5 CSS
- jQuery: xử lý validation client-side
- Modal & datepicker cho UX tốt

---

## **PHASE 6: Tính năng nâng cao (Extra)** (Tuần 12-14)

| Tính năng            | Công việc                                        |
| -------------------- | ------------------------------------------------ |
| **In tem QR**        | Ghép library QRCode Generator + in PDF           |
| **Nhắc bảo trì**     | Scheduler (Quartz) + Service email alert         |
| **Dashboard**        | Chart.js: biểu đồ giá trị tài sản theo phòng ban |
| **Hồ sơ bảo hành**   | File upload + lưu trữ attachment                 |
| **Export Excel/PDF** | Apache POI (Excel) + iText (PDF)                 |

---

## **📊 Timeline tổng hợp**

```
Phase 1: [===]      Database Design        (2 tuần)
Phase 2: [===]      Backend CRUD           (2 tuần)
Phase 3: [===]      Allocation & History   (2 tuần)
Phase 4: [===]      Depreciation & Report  (2 tuần)
Phase 5: [=====]    Frontend UI            (3 tuần)
Phase 6: [===]      Advanced Features      (3 tuần)
────────────────────────────────────────────
Total:              ~14 tuần (3.5 tháng)
```

---

## **💡 Lưu ý quan trọng**

✅ **Testing:** Viết unit test cho Service layer từ Phase 2 trở đi  
✅ **Security:** Thêm Spring Security (login) từ Phase 5  
✅ **Validation:** Input validation ở DTO + Entity  
✅ **Error handling:** Global exception handler  
✅ **Code review:** Sprint 2 tuần một lần

---

## **Chi tiết công việc theo tuần**

### **Tuần 1-2 (Phase 1)**

- [ ] Tạo database schema SQL Server
- [ ] Viết SQL scripts tạo bảng
- [ ] Setup Spring Boot project structure
- [ ] Cấu hình pom.xml dependencies
- [ ] Setup application.properties

### **Tuần 3-4 (Phase 2)**

- [ ] Viết Entity classes
- [ ] Cấu hình JPA relationships
- [ ] Viết DTO classes
- [ ] Tạo Repository interfaces
- [ ] Viết TaiSanService (CRUD)
- [ ] Viết TaiSanController
- [ ] Test API endpoints

### **Tuần 5-6 (Phase 3)**

- [ ] Viết PhanBo & LichSuDieuChuyen entities
- [ ] Viết service phân bổ tài sản
- [ ] Implement enum Status
- [ ] Viết service cập nhật trạng thái
- [ ] Viết Controller cho phân bổ
- [ ] Unit test

### **Tuần 7-8 (Phase 4)**

- [ ] Viết KhauHao entity
- [ ] Implement tính khấu hao
- [ ] Viết service báo cáo
- [ ] Viết controller báo cáo
- [ ] Test tính toán khấu hao

### **Tuần 9-11 (Phase 5)**

- [ ] Thiết kế master layout HTML
- [ ] Trang danh sách tài sản + search
- [ ] Trang form thêm/sửa tài sản
- [ ] Trang phân bổ tài sản
- [ ] Trang lịch sử điều chuyển
- [ ] Trang báo cáo
- [ ] Styling CSS Bootstrap
- [ ] User acceptance testing (UAT)

### **Tuần 12-14 (Phase 6)**

- [x] In tem QR code
- [ ] Nhắc lịch bảo trì (Scheduler)
- [x] Dashboard chart
- [ ] Upload hồ sơ bảo hành
- [ ] Export Excel/PDF
- [ ] Final testing & bug fix

---

**Created:** 24/03/2026  
**Project:** Quản lý Tài sản  
**Technology:** Java Spring Boot + SQL Server + Thymeleaf + Bootstrap 5
