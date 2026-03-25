# Hệ thống Quản lý Tài sản (Asset Management System)

**Phiên bản:** 0.0.4-PHASE4 (Phase 4 - Báo cáo Định giá & Khấu hao)  
**Trạng thái:** Đang phát triển (In Development - Phase 4)  
**Ngôn ngữ:** Tiếng Việt  
**Commit hiện tại:** `TBD` - feat: phase 4 - implement valuation and depreciation reports

---

## 📋 Tổng quan dự án

Hệ thống Quản lý Tài sản là một ứng dụng web được phát triển để quản lý toàn bộ tài sản của tổ chức/doanh nghiệp.

### ✅ Đã Hoàn thành (Completed)

- ✅ **Quản lý Phòng ban:** CRUD đầy đủ (Create, Read, Update, Delete)
- ✅ **Quản lý Tài sản:** CRUD, theo dõi trạng thái, vị trí hiện tại
- ✅ **Phân loại tài sản:** Tạo, quản lý danh mục tài sản
- ✅ **Giao diện web:** Bootstrap 5.3 responsive design
- ✅ **Database Entities:** Department, Asset, AssetCategory, Employee, DepreciationHistory, AssetTransfer
- ✅ **Báo cáo Thống kê:** Định giá danh mục, tính khấu hao hàng tháng, biểu đồ trực quan (Chart.js)
- ✅ **REST API:** Toàn bộ endpoints cho CRUD operations
- ✅ **Phase 3 - Asset Transfer UI:** Modal form điều chuyển tài sản giữa phòng ban
- ✅ **Phase 3 - History Table:** Bảng hiển thị lịch sử điều chuyển tài sản

### 🔄 Đang phát triển (In Progress)

- 🔄 **Form Validation:** Client-side & server-side, error messages chi tiết
- 🔄 **Error Handling:** Professional notifications (Preparing Toastr system)
- 🔄 **Transfer Logic:** Xử lý logic điều chuyển & lịch sử

### ❌ Chưa bắt đầu (TODO)

- 🔲 **QR Code:** Tra cứu tài sản bằng QR code
- 🔲 **Upload ảnh:** Lưu trữ & hiển thị ảnh tài sản
- 🔲 **Excel export:** Xuất dữ liệu sang Excel
- 🔲 **Authentication:** Login/Logout, phân quyền
- 🔲 **Báo cáo:** Dashboard & báo cáo chi tiết

---

## 🛠️ Công nghệ sử dụng

| Component           | Phiên bản | Mục đích                                |
| ------------------- | --------- | --------------------------------------- |
| **Java**            | 17 LTS    | Ngôn ngữ lập trình chính                |
| **Spring Boot**     | 4.0.4     | Framework web & ORM                     |
| **Spring Data JPA** | 4.0.4     | ORM, quản lý database                   |
| **PostgreSQL**      | 12+       | Database chính                          |
| **Thymeleaf**       | 3.1+      | Template engine (Server-side rendering) |
| **Bootstrap**       | 5.3.0     | CSS Framework (CDN)                     |
| **Font Awesome**    | 6.4.0     | Icon library (CDN)                      |
| **Maven**           | 3.8+      | Build tool                              |
| **Tomcat**          | 11.0.18   | Application Server                      |

---

## ⚙️ Yêu cầu hệ thống

### Tối thiểu

- **CPU:** Intel/AMD 2 Core
- **RAM:** 4 GB
- **Disk:** 2 GB (chương trình + database)
- **JDK:** Java 17 trở lên

### Khuyến nghị

- **CPU:** Intel/AMD 4 Core
- **RAM:** 8 GB
- **Disk:** 10 GB SSD
- **JDK:** Java 17 hoặc Java 21

### Phần mềm cần cài đặt

```bash
✓ JDK 17+
✓ PostgreSQL 12+
✓ Maven 3.8+
✓ Git (tùy chọn)
```

---

## 🎯 Trạng thái phát triển theo Phase

### Phase 1: Core CRUD Operations ✅

- Thiết lập project Spring Boot & dependencies
- Tạo entities & database schema (PostgreSQL)
- Implement REST API cho Department, Asset, AssetCategory, Employee
- Tạo web interface cơ bản với Thymeleaf

### Phase 2: UI Enhancement & Asset Management ✅

- Cải thiện UI với Bootstrap 5 responsive design
- Thêm Font Awesome icons
- Implement asset detail view & action buttons
- Add asset filtering & searching capability

### Phase 3: Asset Transfer & History ✅

- Cải thiện UI với form modal điều chuyển tài sản
- Database schema cho `asset_transfers` (lưu lịch sử điều chuyển)
- Bảng hiển thị lịch sử điều chuyển tài sản
- Tích hợp asset movement tracking logic

### Phase 4: Dashboard & Reports (🔄 CURRENT)

**Status:** In Development  
**Commit:** `TBD` - "feat: phase 4 - implement valuation and depreciation reports"

#### ✅ Đã hoàn thành:

- API & Logic tính toán khấu hao hàng tháng tự động (`/api/depreciation/calculate-all`)
- Báo cáo định giá danh mục tài sản theo trạng thái/loại (`/api/reports/valuation`)
- Giao diện Dashboard thống kê báo cáo
- Tích hợp biểu đồ Chart.js trực quan (Pie chart, Bar chart)
- Sửa lỗi mapping JPA Entity `depreciation_history`
- Export báo cáo định giá sang file Excel với Apache POI
- Export báo cáo khấu hao sang file Excel với Apache POI

#### 🔄 Chuẩn bị/Đang làm:

- Professional error handling (Toastr notification system)
- Cải thiện form validation (client + server)
- Export các báo cáo khác sang Excel/PDF
- Tra cứu lịch sử bằng khoảng thời gian

---

## 🚀 Hướng dẫn cài đặt & chạy

### 1. Clone/Download dự án

```bash
# Nếu dùng git
git clone <repository-url>
cd quanlytaisan

# Hoặc download file zip và giải nén
```

### 2. Cấu hình database (PostgreSQL)

#### a) Tạo database

```sql
CREATE DATABASE quanlytaisan;
```

#### b) Cập nhật thông tin kết nối trong `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/quanlytaisan
spring.datasource.username=postgres
spring.datasource.password=<YOUR_PASSWORD>  # Thay bằng mật khẩu của bạn
```

#### c) Tự động tạo bảng (Hibernate DDL-auto)

Ứng dụng sẽ tự động tạo bảng khi khởi động lần đầu:

```properties
spring.jpa.hibernate.ddl-auto=update  # Sẽ tạo bảng nếu chưa tồn tại
```

### 3. Build dự án

```bash
# Nếu sử dụng Maven Wrapper (Windows)
.\mvnw.cmd clean package -DskipTests

# Hoặc Linux/Mac
./mvnw clean package -DskipTests
```

### 4. Chạy ứng dụng

#### Cách 1: Dùng Spring Boot Maven Plugin

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

#### Cách 2: Chạy file JAR

```bash
cd target
java -jar quanlytaisan-0.0.1-SNAPSHOT.jar
```

### 5. Truy cập ứng dụng

- 🌐 **URL:** http://localhost:8080/quanlytaisan/
- 📋 **Danh sách Phòng ban:** http://localhost:8080/quanlytaisan/phong-ban
- 📦 **Danh sách Tài sản:** http://localhost:8080/quanlytaisan/tai-san

---

## 🔗 API Endpoints (REST)

### 1. Department (Phòng ban)

```
GET    /api/departments              - Lấy danh sách toàn bộ phòng ban
GET    /api/departments/{id}         - Lấy chi tiết phòng ban
POST   /api/departments              - Tạo phòng ban mới
PUT    /api/departments/{id}         - Cập nhật thông tin phòng ban
DELETE /api/departments/{id}         - Xóa phòng ban
```

### 2. Asset (Tài sản)

```
GET    /api/assets                   - Lấy danh sách toàn bộ tài sản
GET    /api/assets/{id}              - Lấy chi tiết tài sản
POST   /api/assets                   - Tạo tài sản mới
PUT    /api/assets/{id}              - Cập nhật tài sản
DELETE /api/assets/{id}              - Xóa tài sản
```

### 3. Asset Category (Loại tài sản)

```
GET    /api/asset-categories         - Lấy danh sách loại tài sản
GET    /api/asset-categories/{id}    - Lấy chi tiết loại tài sản
POST   /api/asset-categories         - Tạo loại tài sản mới
PUT    /api/asset-categories/{id}    - Cập nhật loại tài sản
DELETE /api/asset-categories/{id}    - Xóa loại tài sản
```

### 4. Asset Transfer (Điều chuyển tài sản) - PHASE 3

```
GET    /api/asset-transfers          - Lấy lịch sử tất cả điều chuyển
GET    /api/asset-transfers/{id}     - Lấy chi tiết 1 lần điều chuyển
POST   /api/asset-transfers          - Ghi nhận điều chuyển mới (Phase 3)
```

---

## 💾 Hướng dẫn sử dụng

### Quản lý Phòng ban (`/phong-ban`)

1. Click "Thêm phòng ban mới" để mở form
2. Nhập **Mã Phòng** (unique, VD: IT, HR), **Tên Phòng**, **Mô tả**
3. Click "Lưu" → dữ liệu được lưu vào database
4. Sửa/Xóa phòng ban qua action buttons trên bảng

### Quản lý Tài sản (`/tai-san`)

1. Click "Thêm tài sản mới" để mở form
2. Điền thông tin: mã tài sản, tên, giá trị, loại, phòng ban hiện tại
3. Click "Lưu" → tài sản được lưu
4. Xem chi tiết, sửa, xóa, hoặc **Điều chuyển** tài sản

### Điều chuyển Tài sản (Phase 3 Feature)

1. Từ danh sách tài sản, click nút "Điều chuyển"
2. Mở modal form, chọn **Phòng ban đích**, nhập **Lý do**
3. Click "Xác nhận" → lưu lịch sử điều chuyển
4. Xem lịch sử trong bảng "Transfer History" dưới cùng

---

## 📁 Cấu trúc dự án

```
quanlytaisan/
├── src/main/
│   ├── java/com/nhom18/quanlytaisan/
│   │   ├── controller/
│   │   │   ├── DepartmentController.java         # REST API Phòng ban
│   │   │   ├── AssetController.java              # REST API Tài sản
│   │   │   ├── AssetCategoryController.java      # REST API Loại tài sản
│   │   │   ├── EmployeeController.java           # REST API Nhân viên
│   │   │   ├── DepartmentWebController.java      # Web Controller (Thymeleaf)
│   │   │   └── AssetWebController.java           # Web Controller (Thymeleaf)
│   │   ├── service/
│   │   │   ├── DepartmentService.java            # Interface Service
│   │   │   ├── DepartmentServiceImpl.java         # Implement Service
│   │   │   ├── AssetService.java
│   │   │   ├── AssetServiceImpl.java
│   │   │   ├── AssetCategoryService.java
│   │   │   ├── AssetCategoryServiceImpl.java
│   │   │   ├── EmployeeService.java
│   │   │   └── EmployeeServiceImpl.java
│   │   ├── repository/
│   │   │   ├── DepartmentRepository.java         # JPA Repository
│   │   │   ├── AssetRepository.java
│   │   │   ├── AssetCategoryRepository.java
│   │   │   └── EmployeeRepository.java
│   │   ├── entity/
│   │   │   ├── Department.java                   # JPA Entity (Database mapping)
│   │   │   ├── Asset.java
│   │   │   ├── AssetCategory.java
│   │   │   └── Employee.java
│   │   ├── dto/
│   │   │   ├── DepartmentDTO.java                # Data Transfer Object
│   │   │   ├── AssetDTO.java                     # Contains asset info & current location
│   │   │   ├── AssetCategoryDTO.java
│   │   │   ├── EmployeeDTO.java
│   │   │   ├── AssetTransferDTO.java             # DTO for transfers [Phase 3]
│   │   │   └── DepreciationHistoryDTO.java       # DTO for depreciation tracking
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java       # Centralized error handling
│   │   └── QuanlytaisanApplication.java          # Main entry point
│   ├── resources/
│   │   ├── application.properties                # DB, port, JPA settings
│   │   ├── templates/
│   │   │   ├── departments.html                  # Phòng ban - CRUD modal
│   │   │   ├── assets.html                       # Tài sản - CRUD, transfer [Phase 3]
│   │   │   ├── baocao/                           # [TODO] Report templates
│   │   │   └── layouts/                          # [TODO] Shared components
│   │   └── static/
│   │       ├── css/
│   │       │   └── style.css                     # Custom Bootstrap overrides
│   │       ├── js/
│   │       │   ├── asset.js                      # Asset CRUD & transfer operations
│   │       │   └── department.js                 # Department CRUD operations
│   │       └── images/                           # Static assets (logos, etc)
├── pom.xml                                        # Maven configuration
├── README.md                                      # File này
└── CHANGELOG.md                                   # Lịch sử thay đổi [TBD]
```

---

## 🗄️ Sơ đồ Database (PostgreSQL)

### Bảng chính

#### `departments` (Phòng ban)

```sql
┌─────────────────────────────┐
│       departments           │
├─────────────────────────────┤
│ id (PK) - UUID/Long         │
│ code - VARCHAR (Unique)     │
│ name - VARCHAR              │
│ description - TEXT          │
│ created_at - TIMESTAMP      │
└─────────────────────────────┘
```

#### `asset_categories` (Loại tài sản)

```sql
┌──────────────────────────────────┐
│     asset_categories             │
├──────────────────────────────────┤
│ id (PK)                          │
│ category_code - VARCHAR (Unique) │
│ name - VARCHAR                   │
│ default_useful_life_months       │
│ description - TEXT               │
│ created_at - TIMESTAMP           │
└──────────────────────────────────┘
```

#### `employees` (Nhân viên)

```sql
┌────────────────────────────────┐
│        employees               │
├────────────────────────────────┤
│ id (PK)                        │
│ employee_code - VARCHAR        │
│ full_name - VARCHAR            │
│ job_title - VARCHAR            │
│ department_id (FK)             │
│ created_at - TIMESTAMP         │
└────────────────────────────────┘
```

#### `assets` (Tài sản)

```sql
┌────────────────────────────────────┐
│          assets                    │
├────────────────────────────────────┤
│ id (PK)                            │
│ asset_code - VARCHAR (Unique)      │
│ qr_code_text - VARCHAR             │
│ name - VARCHAR                     │
│ purchase_price - DECIMAL           │
│ current_value - DECIMAL            │
│ purchase_date - DATE               │
│ usage_start_date - DATE            │
│ status - ENUM (ACTIVE/INACTIVE)    │
│ category_id (FK)                   │
│ current_department_id (FK)         │
│ current_employee_id (FK)           │
│ warranty_provider - VARCHAR        │
│ warranty_expiry_date - DATE        │
│ created_at - TIMESTAMP             │
└────────────────────────────────────┘
```

#### `asset_transfers` (Lịch sử điều chuyển)

```sql
┌──────────────────────────────────┐
│     asset_transfers              │
├──────────────────────────────────┤
│ id (PK)                          │
│ asset_id (FK)                    │
│ from_department_id (FK)          │
│ to_department_id (FK)            │
│ transfer_date - TIMESTAMP        │
│ reason - VARCHAR                 │
└──────────────────────────────────┘
```

#### `maintenance_records` (Lịch sử bảo trì)

```sql
┌──────────────────────────────────┐
│    maintenance_records           │
├──────────────────────────────────┤
│ id (PK)                          │
│ asset_id (FK)                    │
│ maintenance_date - DATE          │
│ maintenance_cost - DECIMAL       │
│ notes - TEXT                     │
│ next_maintenance_date - DATE     │
└──────────────────────────────────┘
```

#### `depreciation_history` (Lịch sử khấu hao)

```sql
┌──────────────────────────────────┐
│    depreciation_history          │
├──────────────────────────────────┤
│ id (PK)                          │
│ asset_id (FK)                    │
│ depreciation_month - INT         │
│ depreciation_amount - DECIMAL    │
│ accumulated_depreciation - DECIMAL
│ book_value - DECIMAL             │
│ depreciation_date - DATE         │
└──────────────────────────────────┘
```

### Mối quan hệ (Relationships)

```
departments (1) ──────────── (many) employees
     │                              │
     │ (1)                          │ (many)
     ├───────── (many) assets ──────┘
     │
     └─────── (many) asset_transfers

asset_categories (1) ──────── (many) assets

assets (1) ──────────── (many) maintenance_records
     │                  (many) depreciation_history
     │                  (many) asset_transfers
     │
     └─ current_department (FK)
     └─ current_employee (FK)
```

---

## 🎨 UI/UX Design System

### Color Scheme

- **Primary:** Purple gradient (#667eea → #764ba2)
- **Success:** Green (#28a745)
- **Danger:** Red (#dc3545)
- **Warning:** Yellow (#ffc107)
- **Info:** Cyan (#17a2b8)

### Typography

- **Font Family:** Segoe UI, Tahoma, Geneva, Verdana, sans-serif
- **Body:** 14px/16px
- **Headings:** 18px - 32px
- **Icons:** Font Awesome 6.4.0

### Responsive Breakpoints

- Mobile: < 576px
- Tablet: 576px - 992px
- Desktop: > 992px

### Components

- **Navbar:** Gradient purple, fixed-top with navigation
- **Cards:** Shadow, border-radius 8px, hover effects
- **Tables:** Hover color, striped rows, responsive
- **Modals:** Bootstrap 5 modal with form validation
- **Buttons:** Primary (gradient), secondary, warning, danger
- **Forms:** Bootstrap form-control with focus effects

---

## 🧪 Testing & Quality Assurance

### Cần thực hiện

- [ ] Unit tests cho Service layer
- [ ] Integration tests cho API endpoints
- [ ] UI/UX testing (manual)
- [ ] Database transaction testing
- [ ] Error scenario testing

---

## 📝 Lưu ý cho AI Developers

### Context Khi Đọc Code

1. **Architecture:** MVC Pattern (Model-View-Controller)
   - Controller = REST API + Web Controllers
   - Service = Business logic
   - Repository = Data access (JPA)
   - Entity = Database models

2. **Technology Stack:**
   - Backend: Spring Boot 4.0.4 + Spring Data JPA
   - Frontend: Thymeleaf templates + Bootstrap 5
   - Database: PostgreSQL
   - Build: Maven

3. **Current Phase (Phase 3):**
   - Focus: Asset Transfer UI & History
   - Main changes: asset_transfers table, transfer modal, history display
   - Next: Error handling (Toastr), advanced validation

4. **UI/UX Pattern:**
   - Modal forms for CRUD operations
   - Action buttons (Edit, Delete, Transfer)
   - Real-time validation feedback
   - Responsive Bootstrap layout

5. **API Convention:**
   - RESTful endpoints: `/api/{resource}`
   - Request: JSON body
   - Response: JSON (data or error)
   - HTTP methods: GET, POST, PUT, DELETE

### Common Issues

- DB connection: Check application.properties (postgresql URL, credentials)
- UI not loading: Check Thymeleaf templates & CDN links (Bootstrap, Font Awesome)
- API not working: Check controller mappings & service implementations
- Form errors: See browser console & application.properties logging

### File to Read First

1. **README.md** - Overview & setup (you are here!)
2. **application.properties** - Database & server config
3. **DepartmentController.java** - Example REST API pattern
4. **departments.html** - Example Thymeleaf template
5. **department.js** - Example JavaScript CRUD operations

---

## 🚀 Quick Commands

```bash
# Build project
.\mvnw.cmd clean package -DskipTests

# Run application
.\mvnw.cmd spring-boot:run

# Check git status
git status

# View recent commits
git log --oneline -10

# View current branch
git branch -a
```

---

## ✨ Known Issues & Limitations

1. **No Authentication:** All users have full access
2. **No Pagination:** Returns all data (performance issue with large datasets)
3. **Limited Validation:** Basic frontend validation only
4. **No Image Upload:** Asset details support text only
5. **No Backup Strategy:** Database needs manual backup setup
6. **No Audit Trail:** No user tracking for changes

---

## 📋 Getting Started Checklist for New Developers

- [ ] Read this README.md completely
- [ ] Check application.properties for DB settings
- [ ] Examine DepartmentController.java for API pattern
- [ ] Review departments.html for UI pattern
- [ ] Check department.js for JavaScript interactions
- [ ] Run application: `.\mvnw.cmd spring-boot:run`
- [ ] Test API: GET http://localhost:8080/api/departments
- [ ] Test UI: http://localhost:8080/quanlytaisan/phong-ban
- [ ] Review git history: `git log --oneline`
- [ ] Understand Phase 3 features (transfers & history)

---

## 📞 Documentation

For more details:

- **Entity Models:** See `/entity/` packages
- **API Logic:** See `/controller/` & `/service/`
- **Database Schema:** See `@Entity` classes & table definitions above
- **Frontend:** See `templates/` & `static/js/`
- **Git History:** Run `git log --all --graph --decorate`

**Team:** Nhóm 18 | **Status:** Phase 3 In Development | **Branch:** phase3-development

      private final DepartmentRepository repository;
      private final DepartmentMapper mapper;

      // Constructor Dependency Injection
      public DepartmentServiceImpl(
          DepartmentRepository repository,
          DepartmentMapper mapper
      ) {
          this.repository = repository;
          this.mapper = mapper;
      }

}

```

### 📋 Quy tắc mã hoá (Naming Convention)

- Entity class: `DepartmentEntity.java` hoặc `Department.java`
- DTO class: `DepartmentDTO.java`
- Service interface: `DepartmentService.java`
- Service implement: `DepartmentServiceImpl.java`
- Repository: `DepartmentRepository.java`
- Controller (REST): `DepartmentController.java`
- Web Controller: `DepartmentWebController.java`

### 📂 Mẫu cấu trúc folder

```

src/main/java/com/nhom18/quanlytaisan/
├── controller/ (đặt @Controller & @RestController ở đây)
├── service/ (đặt interface & implementation)
├── repository/ (đặt JPA Repository)
├── entity/ (đặt JPA Entity)
├── dto/ (đặt DTO classes)
├── exception/ (đặt custom exception)
└── utils/ (đặt utility & constants)

````

---

## 📊 Tiến độ dự án (Progress Tracker)

### Hoàn thành ✅

- [x] **Phase 1: Setup cấu trúc**
  - [x] Tạo Spring Boot project với Maven
  - [x] Cấu hình pom.xml (dependencies cho JPA, PostgreSQL, Thymeleaf)
  - [x] Cấu hình application.properties (DB connection, JPA, Thymeleaf)
  - [x] Tạo database PostgreSQL & bảng
  - [x] Thêm sample data

- [x] **Phase 2: Backend CRUD**
  - [x] Tạo Entity classes (Department, AssetCategory, Employee, Asset)
  - [x] Tạo Repository interfaces (JPA)
  - [x] Tạo DTO classes
  - [x] Tạo Service interfaces & implementations
  - [x] Tạo REST Controllers (27 endpoints)
  - [x] Manual mapping Entity ↔ DTO

- [x] **Phase 5 (MVP): Frontend - Danh sách**
  - [x] AssetWebController.java
  - [x] DepartmentWebController.java
  - [x] departments.html (Thymeleaf + CSS/JS tách file)
  - [x] assets.html (Thymeleaf + CSS/JS tách file)
  - [x] style.css (CSS tùy chỉnh)
  - [x] asset.js (JavaScript handlers ES6 AssetManager class)

- [x] **Phase 5 (Bug Fix): Frontend Form Validation & API**
  - [x] Sửa lỗi HTML5 form validation (xoá `step="1000"` từ input fields)
  - [x] Sửa lỗi contextPath API endpoint (thêm `th:inline="javascript"`)
  - [x] Cập nhật status codes từ tiếng Anh sang tiếng Việt:
    - [x] `ACTIVE` → `DANG_SUDUNG` (Đang sử dụng)
    - [x] `INACTIVE` → `TRONG_KHO` (Trong kho)
    - [x] `MAINTENANCE` → `BAO_TRI` (Bảo trì)
    - [x] `SCRAP` → `THANH_LY` (Thanh lý)
  - [x] Thêm fallback status display cho dữ liệu cũ trong DB
  - [x] Cập nhật form select options để phù hợp với DB schema

### Đang phát triển 🔄

- [ ] **Phase 5 (Hoàn thiện): Modal Add/Edit/Delete**
  - [ ] Modal form thêm tài sản (phía client đã hoàn thành, đang hoàn thiện validation)
  - [ ] Modal form sửa tài sản
  - [ ] Modal xác nhận xóa
  - [ ] Validation form (Client-side & Server-side)

- [ ] **Phase 3: Quản lý phân bổ & trạng thái**
  - [ ] Tạo entity AssetTransfer
  - [ ] Service & Controller cho chuyển tài sản
  - [ ] Lịch sử điều chuyển

- [x] **Phase 4: Tính khấu hao & Báo cáo**
  - [x] Entity DepreciationHistory
  - [x] Service tính khấu hao
  - [x] Tạo báo cáo tài sản
  - [x] Export báo cáo (Excel định giá)

- [ ] **Phase 6: Tính năng nâng cao**
  - [ ] QR Code sinh & quét
  - [ ] Export Excel danh sách tài sản
  - [ ] Dashboard thống kê
  - [ ] Báo cáo khấu hao
  - [ ] Thông báo (Email/SMS) bảo trì sắp tới

---

## 🗺️ Roadmap (Lộ trình tương lai)

| Giai đoạn   | Kỳ vọng                          | Ưu tiên  |
| ----------- | -------------------------------- | -------- |
| **Q2 2026** | Hoàn thiện Phase 5 (Modal forms) | 🔴 Cao   |
| **Q3 2026** | Phase 3, 4 (Quản lý & Báo cáo)   | 🔴 Cao   |
| **Q4 2026** | Phase 6 (QR Code, Export)        | 🟡 Trung |
| **2027**    | Mobile app (Native/Flutter)      | 🟢 Thấp  |

---

## 📚 Hướng dẫn phát triển thêm (How to Contribute)

### Quy trình phát triển Feature mới

1. **Tạo Branch**

   ```bash
   git checkout -b feature/xxx
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

Dự án này là tài sản của [Công ty/Tổ chức]. Cấm sao chép, sử dụng cho mục đích thương mại mà không được phép.

---

**Cập nhật lần cuối:** 24/03/2026  
**Tác giả:** Team Lead / Product Manager  
**Phiên bản:** 1.0.0
