# Hệ thống Quản lý Tài sản (Asset Management System)

**Phiên bản:** 0.0.5-PHASE5  
**Status:** Đang phát triển  
**Ngôn ngữ:** Java 17 - Spring Boot 4.0  
**Database:** PostgreSQL 12+

## 📋 Tổng quan dự án

Ứng dụng web quản lý tài sản tổ chức/doanh nghiệp với đầy đủ tính năng CRUD, định giá, khấu hao, quét QR, bảo hành, báo cáo Excel/PDF.

### ✅ Tính năng chính

- **CRUD đầy đủ:** Quản lý phòng ban, tài sản, loại tài sản, nhân viên
- **Điều chuyển & Lịch sử:** Theo dõi lịch sử di chuyển tài sản giữa các phòng ban
- **Quét QR:** Camera tích hợp để quét mã QR tài sản, in tem A4
- **Báo cáo & Xuất file:** Định giá danh mục, khấu hao hàng tháng, biểu đồ Chart.js, xuất Excel/PDF
- **Bảo hành & Bảo trì:** Quản lý hồ sơ bảo hành, nhắc lịch bảo trì hàng ngày
- **REST API:** Endpoints CRUD đầy đủ cho tích hợp hệ thống
- **Responsive UI:** Bootstrap 5.3, động responsive trên mobile/tablet/desktop

---

## 🛠️ Công nghệ sử dụng

| Thành phần      | Phiên bản | Mục đích                |
| --------------- | --------- | ----------------------- |
| Java            | 17 LTS    | Ngôn ngữ lập trình      |
| Spring Boot     | 4.0.4     | Framework web           |
| Spring Data JPA | 4.0.4     | ORM, quản lý database   |
| PostgreSQL      | 12+       | Database                |
| Thymeleaf       | 3.1+      | Server-side templating  |
| Apache POI      | 5.2.3     | Xuất Excel              |
| OpenPDF         | 1.3.41    | Xuất PDF                |
| Bootstrap       | 5.3.0     | CSS Framework (CDN)     |
| Chart.js        | 3.9.1     | Biểu đồ trực quan (CDN) |
| Maven           | 3.8+      | Build tool              |

---

## ⚙️ Yêu cầu hệ thống

**Tối thiểu:** CPU 2 core, RAM 4GB, Disk 2GB, JDK 17+  
**Khuyến nghị:** CPU 4 core, RAM 8GB, Disk 10GB SSD, JDK 17/21

**Cần cài đặt:** JDK 17+, PostgreSQL 12+, Maven 3.8+

---

## 🚀 Hướng dẫn cài đặt & chạy

### 1. Clone dự án

```bash
git clone https://github.com/dangngocthien/quanlytaisan.git
cd quanlytaisan
```

### 2. Cấu hình Database (PostgreSQL)

```sql
CREATE DATABASE quanlytaisan;
```

**Cập nhật `application.properties`:**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/quanlytaisan
spring.datasource.username=postgres
spring.datasource.password=<YOUR_PASSWORD>
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build dự án

```bash
# Windows
.\mvnw.cmd clean package -DskipTests

# Linux/Mac
./mvnw clean package -DskipTests
```

### 4. Chạy ứng dụng

```bash
# Cách 1: Maven
.\mvnw.cmd spring-boot:run

# Cách 2: Java JAR
java -jar target/quanlytaisan-*.jar
```

### 5. Truy cập

- **URL chính:** http://localhost:8080/quanlytaisan/
- **Phòng ban:** http://localhost:8080/quanlytaisan/phong-ban
- **Tài sản:** http://localhost:8080/quanlytaisan/tai-san
- **Báo cáo:** http://localhost:8080/quanlytaisan/baocao

---

## 🔗 API Endpoints (REST)

### Phòng ban & Tài sản

```
GET    /api/departments              - Danh sách phòng ban
POST   /api/departments              - Tạo phòng ban
PUT    /api/departments/{id}         - Cập nhật phòng ban
DELETE /api/departments/{id}         - Xóa phòng ban

GET    /api/assets                   - Danh sách tài sản
POST   /api/assets                   - Tạo tài sản
PUT    /api/assets/{id}              - Cập nhật tài sản
DELETE /api/assets/{id}              - Xóa tài sản

GET    /api/asset-categories         - Danh sách loại tài sản
POST   /api/asset-categories         - Tạo loại tài sản
```

### Điều chuyển & Báo cáo

```
GET    /api/asset-transfers          - Lịch sử điều chuyển
POST   /api/asset-transfers          - Ghi nhận điều chuyển

GET    /api/depreciation/calculate-all - Tính khấu hao tất cả tài sản
GET    /api/reports/valuation         - Báo cáo định giá danh mục
```

---

## 💾 Hướng dẫn sử dụng

### Quản lý Phòng ban (`/phong-ban`)

1. Click "Thêm phòng ban mới"
2. Nhập Mã phòng, Tên phòng, Mô tả
3. Lưu → Dữ liệu lưu vào database
4. Sửa/Xóa qua action buttons

### Quản lý Tài sản (`/tai-san`)

1. Click "Thêm tài sản"
2. Điền: mã tài sản, tên, giá, loại, phòng ban
3. Lưu → Tài sản được tạo
4. Điều chuyển: Click "Điều chuyển" → Chọn phòng đích → Lưu lịch sử

### Quét QR & In Tem

1. **Quét:** Click "Quét QR" → Camera mở → Quét mã QR trên tài sản
2. **In tem:** Click "In tem" → Chọn tài sản → PDF A4 tối ưu in
3. **Tải QR:** Mỗi tài sản có nút tải mã QR PNG

### Báo cáo & Xuất file

1. **Dashboard:** Xem biểu đồ định giá, khấu hao
2. **Xuất Excel:** Click "Xuất Excel" → Download file báo cáo
3. **Xuất PDF:** Click "Xuất PDF" → Download báo cáo định dạng PDF

---

## 📁 Cấu trúc dự án

```
src/main/
├── java/com/nhom18/quanlytaisan/
│   ├── controller/          # REST & Web Controllers
│   ├── service/             # Business Logic (Interface + Impl)
│   ├── repository/          # JPA Repositories
│   ├── entity/              # JPA Entities (DB mapping)
│   ├── dto/                 # Data Transfer Objects
│   ├── scheduler/           # Background tasks (bảo trì)
│   ├── config/              # Security & Configuration
│   ├── exception/           # Exception handling
│   └── QuanlytaisanApplication.java
├── resources/
│   ├── application.properties
│   ├── templates/           # Thymeleaf HTML
│   │   ├── assets.html
│   │   ├── departments.html
│   │   └── baocao/          # Report pages
│   └── static/
│       ├── css/style.css
│       └── js/              # Asset, Department CRUD JS
```

---

## 🗄️ Database Schema chính

**Bảng Departments** - Quản lý phòng ban  
**Bảng Assets** - Quản lý tài sản (mã, tên, giá, loại, phòng hiện tại)  
**Bảng Asset_Categories** - Loại tài sản (máy tính, nội thất, v.v.)  
**Bảng Employees** - Nhân viên quản lý  
**Bảng Asset_Transfers** - Lịch sử di chuyển tài sản  
**Bảng Maintenance_Records** - Lịch sử bảo trì, bảo hành  
**Bảng Depreciation_History** - Lịch sử khấu hao hàng tháng

---

## 🎯 Trạng thái phát triển

| Phase | Tính năng                                 | Status |
| ----- | ----------------------------------------- | ------ |
| 1     | Core CRUD, Spring Boot, PostgreSQL        | ✅     |
| 2     | UI Bootstrap, Thymeleaf, Asset management | ✅     |
| 3     | Asset Transfer, History tracking          | ✅     |
| 4     | Dashboard, Reports, Depreciation          | ✅     |
| 5     | QR Code, QR Scanner, In tem, Bảo hành     | ✅     |

---

## 📝 Hướng dẫn phát triển

### Cấu trúc code

- **Controllers:** Handle HTTP requests → gọi Service
- **Services:** Business logic, validation → gọi Repository
- **Repositories:** Database queries (JPA)
- **DTOs:** Transfer data giữa Controller ↔ Frontend
- **Entities:** JPA mapping với database tables

### Chạy tests

```bash
mvnw test
```

### Build production

```bash
mvnw clean package
```

---

## 🤝 Đóng góp

Fork → Tạo branch tính năng (`git checkout -b feature/AmazingFeature`) → Commit → Push → Tạo Pull Request

---

## 📞 Liên hệ & Hỗ trợ

**Repository:** https://github.com/dangngocthien/quanlytaisan  
**Issues:** Báo lỗi qua GitHub Issues  
**Email:** [Thêm email hỗ trợ]

---

## 📄 License

MIT License - Xem file LICENSE để chi tiết

## 📚 Hướng dẫn phát triển thêm (How to Contribute)

### Quy trình phát triển Feature mới

1. **Tạo Branch**

   ```bash
   git checkout -b feature/xxx
   ```

````

2. **Phát triển bao gồm:**
   - ✅ Entity (nếu cần bảng DB mới)
   - ✅ Repository (custom queries)
   - ✅ DTO (data transfer)
   - ✅ Service (interface + impl)
   - ✅ Controller (REST API)
   - ✅ Web Controller (nếu cần UI)
   - ✅ Template HTML (nếu cần)

3. **Testing**

   ```bash
   .\mvnw.cmd test
   ```

4. **Build & Package**

   ```bash
   .\mvnw.cmd clean package -DskipTests
   ```

5. **Push & Merge Request**

---

## 🐛 Troubleshooting

### Lỗi: "java.net.SocketTimeoutException: Receive timed out"

**Nguyên nhân:** PostgreSQL không kết nối được
**Giải pháp:**

- Kiểm tra PostgreSQL đã chạy: `pg_isready`
- Kiểm tra username/password trong application.properties
- Kiểm tra database đã tồn tại: `createdb quanlytaisan`

### Lỗi: "Table already exists"

**Nguyên nhân:** Schema đã tồn tại từ lần chạy trước
**Giải pháp:**

```sql
DROP DATABASE quanlytaisan;
CREATE DATABASE quanlytaisan;
```

### Port 8080 đã bị chiếm

```bash
# Windows: Tìm process dùng port 8080
netstat -ano | findstr :8080

# Kill process (thay PID phù hợp)
taskkill /PID <PID> /F

# Linux:
lsof -i :8080
kill -9 <PID>
```

---

## 📞 Liên hệ & Hỗ trợ

- 👥 **Team Lead:** Nhóm 18
- 📧 **Email:** nhom18@example.com
- 💬 **Slack:** #asset-management-system
- 📱 **Hotline:** +84 xxx xxx xxx

---

## 📄 Tài liệu tham khảo

- [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.0/)

---

## 📜 License

Dự án này là tài sản của [nhóm18]. Cấm sao chép, sử dụng cho mục đích thương mại mà không được phép.

---

**Cập nhật lần cuối:** 24/03/2026
**Tác giả:** Team Lead / Product Manager
**Phiên bản:** 1.0.0
````
