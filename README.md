# Hệ thống Quản lý Tài sản (Asset Management System)

**Phiên bản:** 0.0.1-SNAPSHOT  
**Trạng thái:** Đang phát triển (In Development)  
**Ngôn ngữ:** Tiếng Việt

---

## 📋 Tổng quan dự án

Hệ thống Quản lý Tài sản là một ứng dụng web được phát triển để quản lý toàn bộ tài sản của tổ chức/doanh nghiệp. Hệ thống cung cấp các tính năng:

- ✅ Quản lý danh sách tài sản (tạo, xem, sửa, xóa)
- ✅ Phân loại tài sản theo loại (máy tính, văn phòng, điện tử, v.v.)
- ✅ Gán tài sản cho phòng ban & nhân viên
- ✅ Theo dõi trạng thái tài sản (đang sử dụng, bảo trì, thanh lý, v.v.)
- ✅ Tính toán khấu hao
- 🔄 Xuất báo cáo & thống kê
- 🔄 QR Code tra cứu nhanh

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
│   │   │   ├── AssetDTO.java
│   │   │   ├── AssetCategoryDTO.java
│   │   │   └── EmployeeDTO.java
│   │   └── QuanlytaisanApplication.java          # Main entry point
│   ├── resources/
│   │   ├── application.properties                # Cấu hình ứng dụng
│   │   ├── templates/
│   │   │   ├── departments.html                  # Thymeleaf template (Phòng ban)
│   │   │   └── assets.html                       # Thymeleaf template (Tài sản)
│   │   └── static/
│   │       ├── css/
│   │       │   └── style.css                     # CSS tùy chỉnh
│   │       └── js/
│   │           ├── asset.js                      # JavaScript (Module Tài sản)
│   │           └── department.js                 # JavaScript (Module Phòng ban) [TBD]
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
│ period - VARCHAR (YYYY-MM)       │
│ depreciation_amount - DECIMAL    │
│ accumulated_depreciation - DECIMAL│
│ assets_value_after - DECIMAL     │
│ created_at - TIMESTAMP           │
└──────────────────────────────────┘
```

---

## 📡 API Endpoints (REST API)

### Phòng ban (Departments)

```
GET     /api/departments               → Lấy danh sách phòng ban
POST    /api/departments               → Tạo phòng ban mới
GET     /api/departments/{id}          → Lấy chi tiết phòng ban
PUT     /api/departments/{id}          → Cập nhật phòng ban
DELETE  /api/departments/{id}          → Xóa phòng ban
GET     /api/departments/code/{code}   → Tìm phòng ban theo mã
```

### Tài sản (Assets)

```
GET     /api/assets                    → Lấy danh sách tài sản
POST    /api/assets                    → Tạo tài sản mới
GET     /api/assets/{id}               → Lấy chi tiết tài sản
PUT     /api/assets/{id}               → Cập nhật tài sản
DELETE  /api/assets/{id}               → Xóa tài sản
GET     /api/assets/code/{code}        → Tìm tài sản theo mã
GET     /api/assets/category/{catId}   → Lấy tài sản theo loại
GET     /api/assets/department/{deptId}→ Lấy tài sản theo phòng ban
GET     /api/assets/status/{status}    → Lấy tài sản theo trạng thái
```

### Loại tài sản (Asset Categories)

```
GET     /api/asset-categories          → Lấy danh sách loại tài sản
POST    /api/asset-categories          → Tạo loại tài sản mới
GET     /api/asset-categories/{id}     → Lấy chi tiết loại tài sản
PUT     /api/asset-categories/{id}     → Cập nhật loại tài sản
DELETE  /api/asset-categories/{id}     → Xóa loại tài sản
```

### Nhân viên (Employees)

```
GET     /api/employees                 → Lấy danh sách nhân viên
POST    /api/employees                 → Tạo nhân viên mới
GET     /api/employees/{id}            → Lấy chi tiết nhân viên
PUT     /api/employees/{id}            → Cập nhật nhân viên
DELETE  /api/employees/{id}            → Xóa nhân viên
GET     /api/employees/code/{code}     → Tìm nhân viên theo mã
```

---

## 📝 Quy tắc phát triển (Coding Standards) ⚠️ **BẮT BUỘC**

### ✅ Quy tắc bắt buộc tuân thủ

#### 1. **TUYỆT ĐỐI KHÔNG dùng Lombok**

- ❌ **Không được phép:**

  ```java
  @Data
  @Entity
  public class Department {
      // ...
  }
  ```

- ✅ **Bắt buộc viết:**

  ```java
  @Entity
  @Table(name = "departments")
  public class Department {
      private Long id;
      private String code;
      private String name;

      // Tự viết Constructor
      public Department() {}

      public Department(String code, String name) {
          this.code = code;
          this.name = name;
      }

      // Tự viết Getter/Setter
      public Long getId() { return id; }
      public void setId(Long id) { this.id = id; }

      public String getCode() { return code; }
      public void setCode(String code) { this.code = code; }

      // ... tiếp tục cho các field khác
  }
  ```

#### 2. **Chuyển đổi DTO ↔ Entity hoàn toàn bằng tay**

- ❌ **Không được phép:**

  ```java
  @Autowired
  private ModelMapper modelMapper;

  DepartmentDTO dto = modelMapper.map(entity, DepartmentDTO.class);
  ```

- ✅ **Bắt buộc:**

  ```java
  public class DepartmentServiceImpl implements DepartmentService {

      // Private method để mapping Entity → DTO
      private DepartmentDTO mapEntityToDTO(Department entity) {
          if (entity == null) return null;

          DepartmentDTO dto = new DepartmentDTO();
          dto.setId(entity.getId());
          dto.setCode(entity.getCode());
          dto.setName(entity.getName());
          dto.setDescription(entity.getDescription());
          return dto;
      }

      // Private method để mapping DTO → Entity
      private Department mapDTOToEntity(DepartmentDTO dto) {
          if (dto == null) return null;

          Department entity = new Department();
          entity.setCode(dto.getCode());
          entity.setName(dto.getName());
          entity.setDescription(dto.getDescription());
          return entity;
      }
  }
  ```

#### 3. **Dùng Constructor Injection (KHÔNG dùng @Autowired trên field)**

- ❌ **Không được phép:**

  ```java
  @Service
  public class DepartmentServiceImpl implements DepartmentService {
      @Autowired
      private DepartmentRepository repository;

      @Autowired
      private DepartmentMapper mapper;
  }
  ```

- ✅ **Bắt buộc:**

  ```java
  @Service
  public class DepartmentServiceImpl implements DepartmentService {

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
├── controller/      (đặt @Controller & @RestController ở đây)
├── service/         (đặt interface & implementation)
├── repository/      (đặt JPA Repository)
├── entity/          (đặt JPA Entity)
├── dto/             (đặt DTO classes)
├── exception/       (đặt custom exception)
└── utils/           (đặt utility & constants)
```

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

- [ ] **Phase 4: Tính khấu hao & Báo cáo**
  - [ ] Entity DepreciationHistory
  - [ ] Service tính khấu hao
  - [ ] Tạo báo cáo tài sản
  - [ ] Export báo cáo (PDF/Excel)

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
   ```

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
