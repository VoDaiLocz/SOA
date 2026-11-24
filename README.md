# Hệ Thống Quản Lý Đồ Án Tốt Nghiệp - Kiến Trúc Microservices (SOA)

Dự án xây dựng theo kiến trúc Microservices (Service-Oriented Architecture), áp dụng mô hình Monorepo phân tách rõ ràng 2 phần `backend/` và `frontend/`.

---

## 1. Cấu Trúc Dự Án

```text
├── backend/                  # Chứa toàn bộ mã nguồn 9 Microservices & Aggregator pom.xml
│   ├── api-gateway/          # Cổng API Gateway (Port 8080)
│   ├── auth-service/         # Dịch vụ xác thực JWT, phân quyền (Port 8084)
│   ├── user-service/         # Dịch vụ quản lý Sinh viên, Giảng viên, Khoa, Lớp (Port 8081)
│   ├── detai-service/        # Dịch vụ quản lý Đề tài, Đăng ký đề tài (Port 8082)
│   ├── council-service/      # Dịch vụ quản lý Hội đồng bảo vệ (Port 8083)
│   ├── grading-service/      # Dịch vụ Chấm điểm, tính điểm TB, xếp loại (Port 8085)
│   ├── analytics-service/    # Dịch vụ Thống kê số liệu, xuất báo cáo CSV (Port 8086)
│   ├── notification-service/ # Dịch vụ Thông báo hệ thống (Port 8087)
│   ├── storage-service/      # Dịch vụ Upload/Download file tài liệu (Port 8088)
│   └── pom.xml               # Parent POM quản lý dependency tập trung
├── frontend/                 # Giao diện người dùng Web SPA (Tailwind CSS, REST Client)
│   └── index.html
├── docker-compose.yml        # Điều phối khởi chạy toàn bộ 9 services + MySQL 8.0
├── init.sql                  # Script tạo 8 Database riêng biệt và nạp dữ liệu mẫu
├── pom.xml                   # Root POM liên kết Maven reactor
└── postman/                  # Bộ sưu tập Postman 45 API endpoints kiểm thử
```

---

## 2. Danh Sách Microservices & Cơ Sở Dữ Liệu

Áp dụng chuẩn **Database-per-Service** (mỗi service sở hữu schema riêng, không dùng chung DB):

| Service | Port | Database | Kỹ Thuật Chính | Chức Năng Chính |
| :--- | :---: | :--- | :--- | :--- |
| **api-gateway** | `8080` | — | Spring Cloud Gateway WebFlux | Reverse proxy, định tuyến API, xử lý Global CORS |
| **auth-service** | `8084` | `auth_db` | Spring Security 6, JJWT 0.12.6, BCrypt | Đăng nhập, cấp phát JWT Token, kiểm tra tài khoản |
| **user-service** | `8081` | `user_db` | Spring Data JPA, MySQL | CRUD Giảng viên, Sinh viên, Khoa, Lớp |
| **detai-service** | `8082` | `detai_db` | Spring Cache (In-Memory), OpenFeign | Quản lý đề tài, đăng ký đề tài, cache danh mục |
| **council-service** | `8083` | `council_db` | Spring Data JPA, MySQL | Lập hội đồng bảo vệ, phân công đề tài cho hội đồng |
| **grading-service** | `8085` | `grading_db` | Spring Data JPA, MySQL | Nhập điểm đề cương/bảo vệ, tính ĐTB, xếp loại |
| **analytics-service**| `8086` | `analytics_db` | Spring Cache (In-Memory), CSV Export | Thống kê số lượng đề tài, tỷ lệ đạt, xuất file CSV |
| **notification-service**| `8087`| `notification_db`| Spring Data JPA, MySQL | Gửi thông báo phê duyệt, nhắc hạn, đánh dấu đã đọc |
| **storage-service** | `8088` | `storage_db` | Multipart File, Resource Stream | Upload/Download thuyết minh đề tài, biên bản |

---

## 3. Công Nghệ & Kỹ Thuật Sử Dụng

* **Ngôn ngữ & Nền tảng:** Java 21 / OpenJDK 25, Spring Boot 3.3.4, Spring Cloud 2023.0.3.
* **Mã nguồn sạch (Clean Code):** Viết bằng Standard Java thuần (không dùng Lombok) để tương thích 100% với javac JDK 25 và bảo đảm tính tường minh.
* **Giao tiếp liên dịch vụ (Inter-service):** Spring Cloud OpenFeign (gọi REST đồng bộ giữa `detai-service` và `user-service`).
* **Bảo mật (Security):**
  * Stateless Authentication qua Bearer JWT (thuật toán HMAC-SHA256).
  * Mã hóa mật khẩu 1 chiều bằng BCrypt.
  * Phân quyền theo Role: `ADMIN`, `GIANG_VIEN`, `SINH_VIEN`.
* **Bộ nhớ đệm (Caching):**
  * Kích hoạt Spring In-Memory Cache với `@EnableCaching`.
  * `@Cacheable("danhSachDeTai")` và `@Cacheable("thongKeDeTai")` giúp giảm tải truy vấn DB.
  * `@CacheEvict(allEntries = true)` tự động dọn cache khi thêm/sửa/xóa đề tài.
* **Cơ sở dữ liệu:** MySQL 8.0, cấu hình 8 schema độc lập trong `init.sql`.
* **Đóng gói & Ảo hóa:** Docker, Dockerfile đa tầng trên nền `eclipse-temurin:21-jre-alpine`, Docker Compose.

---

## 4. Hướng Dẫn Cài Đặt & Khởi Chạy

### Yêu cầu tiên quyết
* Đã cài đặt Docker và Docker Compose (hoặc Docker Desktop).
* JDK 21+ và Maven (nếu muốn build thủ công bằng lệnh).

### 1. Khởi chạy toàn bộ hệ thống bằng Docker Compose (Khuyên dùng)
```bash
# Clone dự án
git clone https://github.com/VoDaiLocz/SOA.git
cd SOA

# Build và khởi chạy MySQL cùng 9 Microservices
docker compose up -d --build
```

### 2. Kiểm tra trạng thái các container
```bash
docker compose ps
```

### 3. Build & kiểm tra mã nguồn bằng Maven
```bash
# Build từ thư mục gốc
mvn clean test-compile -DskipTests

# Hoặc build riêng thư mục backend
cd backend
mvn clean test-compile -DskipTests
```

### 4. Mở giao diện Frontend
* Mở trực tiếp file `frontend/index.html` bằng trình duyệt web.
* Giao diện đã được cấu hình tự động kết nối qua API Gateway tại `http://localhost:8080`.

---

## 5. Danh Mục API Endpoints Chính (Qua Gateway Port 8080)

* **Xác thực:**
  * `POST /api/v1/auth/login` - Đăng nhập nhận JWT Token.
* **Sinh viên & Giảng viên:**
  * `GET /api/v1/sinh-vien` - Danh sách sinh viên.
  * `GET /api/v1/giang-vien` - Danh sách giảng viên.
* **Đề tài & Đăng ký:**
  * `GET /api/v1/de-tai` - Danh sách đề tài (có Cache).
  * `POST /api/v1/de-tai` - Tạo mới đề tài (tự động xóa Cache).
  * `POST /api/v1/de-tai/dang-ky` - Sinh viên đăng ký đề tài.
* **Hội đồng & Chấm điểm:**
  * `POST /api/v1/hoi-dong` - Thành lập hội đồng bảo vệ.
  * `POST /api/v1/diem` - Nhập điểm cho đề tài.
  * `GET /api/v1/diem/trung-binh/{maDeTai}` - Xem điểm TB và xếp loại.
* **Thống kê & Tài liệu:**
  * `GET /api/v1/thong-ke/detai` - Thống kê số lượng đề tài theo trạng thái (có Cache).
  * `GET /api/v1/thong-ke/xuat?dinhDang=excel` - Xuất file thống kê CSV.
  * `POST /api/v1/tai-lieu/upload` - Upload file thuyết minh.
  * `GET /api/v1/tai-lieu/{id}/tai-ve` - Download file tài liệu.
