# Hướng dẫn giải thích toàn bộ dự án SOA và Microservices

## Mục đích của tài liệu

Tài liệu này giải thích toàn bộ hệ thống quản lý đồ án tốt nghiệp từ đầu đến cuối: vì sao hệ thống được chia thành nhiều service, API Gateway là gì, Docker Compose triển khai các thành phần như thế nào, các container giao tiếp ra sao, dữ liệu được tổ chức thế nào, và đặc biệt là luồng đăng ký đề tài do `dangky-service` điều phối.

Tài liệu được viết theo thứ tự nên dùng để học và tạo audio:

1. Hiểu bài toán.
2. Hiểu kiến trúc tổng thể.
3. Hiểu API Gateway.
4. Hiểu Docker, container, port và network.
5. Hiểu từng microservice.
6. Hiểu Database-per-Service.
7. Hiểu luồng đăng ký đề tài.
8. Hiểu cách chạy và kiểm thử hệ thống.

---

## 1. Bài toán mà hệ thống giải quyết

Hệ thống quản lý đồ án tốt nghiệp cần quản lý ba nhóm dữ liệu chính:

- Thông tin sinh viên.
- Danh sách đề tài tốt nghiệp.
- Các lượt đăng ký đề tài của sinh viên.

Nếu xây dựng theo kiểu monolith, toàn bộ chức năng có thể nằm trong một ứng dụng duy nhất. Ứng dụng đó sẽ chứa controller, service, repository và database logic cho cả sinh viên, đề tài và đăng ký.

Trong dự án này, hệ thống được tách thành các service độc lập theo từng nhóm nghiệp vụ. Cách tách này giúp mỗi service có trách nhiệm rõ ràng, có mã nguồn riêng, có database riêng và có thể được triển khai độc lập.

---

## 2. SOA và Microservices trong dự án

### 2.1. SOA là gì?

SOA là viết tắt của Service-Oriented Architecture, nghĩa là kiến trúc hướng dịch vụ. Trong kiến trúc này, hệ thống được phân chia thành các dịch vụ có trách nhiệm nghiệp vụ riêng. Các dịch vụ giao tiếp với nhau thông qua những giao diện hoặc API được định nghĩa rõ ràng.

Ví dụ trong dự án:

- Service sinh viên cung cấp API quản lý sinh viên.
- Service đề tài cung cấp API quản lý đề tài.
- Service đăng ký cung cấp API xử lý đăng ký.

Một service không nên truy cập trực tiếp vào database của service khác. Nếu cần dữ liệu của service khác, nó nên gọi API của service đó.

### 2.2. Microservices là gì?

Microservices là cách triển khai các service theo hướng nhỏ, độc lập và có thể chạy riêng. Mỗi service thường có:

- Một codebase hoặc module riêng.
- Một tiến trình riêng.
- Một port riêng.
- Một database hoặc phạm vi dữ liệu riêng.
- Khả năng build và triển khai riêng.

Dự án này có đặc điểm của mô hình microservices:

```text
sinhvien-service
detai-service
dangky-service
api-gateway
```

Mỗi service Spring Boot được đóng gói thành một image và chạy trong một container riêng khi triển khai bằng Docker Compose.

### 2.3. SOA và Microservices có phải là hai thứ hoàn toàn khác nhau không?

Trong phạm vi dự án này, có thể hiểu đơn giản:

- SOA là tư tưởng và kiến trúc tổ chức hệ thống thành các dịch vụ.
- Microservices là cách chia nhỏ và triển khai các dịch vụ đó một cách độc lập.

Dự án sử dụng SOA ở mức giao tiếp giữa các dịch vụ và sử dụng cách triển khai microservices bằng Spring Boot và Docker.

---

## 3. Kiến trúc tổng thể của hệ thống

Hệ thống có thể chia thành ba tầng chính:

```text
Tầng client
    |
    v
API Gateway
    |
    v
Tầng microservice nghiệp vụ
    |
    v
Tầng database
```

Sơ đồ chi tiết:

```text
                         CLIENT
              Browser / Frontend / Postman
                              |
                              | HTTP tới port 8080
                              v
                    +---------------------+
                    |    API Gateway      |
                    |      :8080          |
                    +---------------------+
                       |        |        |
              Docker network: soa-network
                       |        |        |
                       v        v        v
                 +---------+ +---------+ +---------+
                 | SinhVien| | DeTai   | | DangKy  |
                 | :8081   | | :8082   | | :8083   |
                 +---------+ +---------+ +---------+
                     |          |           |
                     v          v           v
                 sinhvien_db detai_db   dangky_db
```

Ý nghĩa của sơ đồ:

- Client gửi request vào API Gateway tại port `8080`.
- Gateway phân tích URL và chuyển request đến service phù hợp.
- Các service nghiệp vụ xử lý logic riêng của mình.
- Mỗi service lưu dữ liệu vào database riêng.
- Các service có thể gọi API của nhau qua mạng nội bộ Docker.

---

## 4. API Gateway là gì?

### 4.1. API Gateway không phải là database và cũng không phải chỉ là một firewall

API Gateway là một ứng dụng trung gian đứng trước các microservice. Trong dự án, ứng dụng này được xây dựng bằng Spring Cloud Gateway.

Có thể hình dung API Gateway như quầy tiếp nhận của một cơ quan:

- Người dùng chỉ cần đến một địa chỉ.
- Quầy tiếp nhận xem yêu cầu thuộc bộ phận nào.
- Quầy chuyển yêu cầu đến đúng bộ phận.
- Kết quả từ bộ phận được trả ngược lại cho người dùng.

API Gateway không tự xử lý nghiệp vụ sinh viên, đề tài hay đăng ký. Nhiệm vụ chính của nó là tiếp nhận và định tuyến request.

### 4.2. Gateway chạy ở đâu?

Gateway là một Spring Boot application riêng:

```text
api-gateway/
```

Ứng dụng này được đóng gói thành container riêng và chạy tại port:

```text
8080
```

Trong Docker Compose, port được publish:

```yaml
api-gateway:
  ports:
    - "8080:8080"
```

Ý nghĩa của dòng trên:

```text
Port 8080 của máy host
        |
        v
Port 8080 của container api-gateway
```

Vì vậy client bên ngoài gọi:

```text
http://localhost:8080
```

### 4.3. Gateway định tuyến bằng URL như thế nào?

Gateway có cấu hình các route trong:

```text
api-gateway/src/main/resources/application.yml
```

Các route chính:

```yaml
- id: sinhvien-service
  uri: http://sinhvien-service:8081
  predicates:
    - Path=/api/v1/sinhvien/**

- id: detai-service
  uri: http://detai-service:8082
  predicates:
    - Path=/api/v1/detai/**

- id: dangky-service
  uri: http://dangky-service:8083
  predicates:
    - Path=/api/v1/dangky/**
```

Ví dụ request:

```text
GET http://localhost:8080/api/v1/sinhvien
```

Gateway nhìn thấy path:

```text
/api/v1/sinhvien
```

Gateway đối chiếu path với route `sinhvien-service`, sau đó chuyển request đến:

```text
http://sinhvien-service:8081/api/v1/sinhvien
```

Tương tự:

```text
/api/v1/detai  → detai-service:8082
/api/v1/dangky  → dangky-service:8083
```

Gateway không cần biết logic bên trong service. Nó chỉ cần biết service đích, hostname và port đích.

### 4.4. Gateway có phải là hệ thống đăng nhập không?

Không phải trong code hiện tại.

API Gateway có thể được mở rộng để kiểm tra JWT, API key, session hoặc quyền truy cập. Tuy nhiên dự án hiện tại chưa triển khai:

- Màn hình đăng nhập.
- JWT.
- Spring Security.
- Session đăng nhập.
- Phân quyền theo role.

Trong phiên bản hiện tại, Gateway chủ yếu làm ba việc:

1. Nhận request tại port `8080`.
2. Định tuyến request theo path.
3. Cấu hình CORS cho các request từ frontend.

### 4.5. Gateway có phải là lý do duy nhất để dùng một cổng duy nhất không?

Không. Việc dùng Gateway còn giúp:

- Frontend chỉ cần biết một base URL.
- Thay đổi địa chỉ nội bộ của service mà không phải sửa frontend.
- Gom CORS về một nơi.
- Có một điểm tập trung để thêm authentication, authorization, rate limiting và logging sau này.
- Che giấu cách tổ chức service phía sau với client bên ngoài.

---

## 5. Frontend giao tiếp với hệ thống như thế nào?

Frontend hiện tại là file tĩnh:

```text
frontend/index.html
```

Frontend không có nhiều trang riêng cho từng service. Nó dùng một trang với ba tab:

- Sinh viên.
- Đề tài.
- Đăng ký đồ án.

Frontend khai báo một địa chỉ Gateway duy nhất:

```javascript
const GATEWAY_BASE_URL = "http://localhost:8080/api/v1";
```

Frontend không gọi trực tiếp:

```text
localhost:8081
localhost:8082
localhost:8083
```

Frontend chỉ gọi:

```text
localhost:8080/api/v1/sinhvien
localhost:8080/api/v1/detai
localhost:8080/api/v1/dangky
```

Khi người dùng bấm nút trên giao diện, JavaScript dùng `fetch()` để gửi HTTP request. Sau khi nhận response, frontend cập nhật bảng dữ liệu hoặc hiển thị thông báo thành công, thất bại.

Ví dụ:

```javascript
fetch(`${GATEWAY_BASE_URL}/dangky`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
});
```

Điều này có nghĩa:

```text
Frontend → Gateway:8080 → dangky-service:8083
```

Frontend không cần biết `dangky-service` nằm ở container nào hoặc IP nội bộ là bao nhiêu.

---

## 6. Docker và container

### 6.1. Container là gì?

Container là một môi trường chạy ứng dụng được cô lập tương đối. Trong dự án:

- Một container chạy `sinhvien-service`.
- Một container chạy `detai-service`.
- Một container chạy `dangky-service`.
- Một container chạy `api-gateway`.
- Một container chạy MySQL.

Mỗi container có process, filesystem và biến môi trường riêng. Container nhẹ hơn máy ảo vì các container dùng chung kernel của hệ điều hành host.

Container không phải là một máy vật lý riêng. Toàn bộ các container trong file Compose thông thường vẫn chạy trên cùng một Docker hoặc Podman host.

### 6.2. Dockerfile làm gì?

Mỗi service có Dockerfile. Ví dụ Gateway sử dụng:

```dockerfile
FROM docker.io/library/eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Ý nghĩa:

1. Chọn môi trường Java 21 JRE.
2. Tạo thư mục làm việc `/app`.
3. Copy file JAR đã build vào container.
4. Khai báo ứng dụng sử dụng port `8080`.
5. Chạy file JAR khi container khởi động.

`EXPOSE` chỉ mô tả port mà ứng dụng dùng trong container. Nó không tự động publish port ra máy host. Muốn host truy cập được, Compose phải có cấu hình `ports`.

### 6.3. Docker Compose làm gì?

File:

```text
docker-compose.yml
```

là bản mô tả cách chạy toàn bộ hệ thống. Nó định nghĩa:

- Các service/container.
- Image hoặc cách build image.
- Biến môi trường.
- Port.
- Volume.
- Network.
- Thứ tự phụ thuộc khi khởi động.

Khi chạy:

```bash
docker compose up -d
```

Compose đọc file YAML, tạo network, tạo hoặc khởi động container, gắn container vào network và áp dụng các biến môi trường.

---

## 7. `soa-network` là gì?

### 7.1. `soa-network` không phải là container

`soa-network` là một mạng ảo do Docker hoặc Podman tạo ra. Nó giống như một mạng LAN nội bộ nối các container.

```text
Container Gateway       ─┐
Container SinhVien      ─┤
Container DeTai         ─┤── soa-network
Container DangKy        ─┘
```

Trong Compose:

```yaml
networks:
  soa-network:
    driver: bridge
```

Và mỗi container tham gia mạng:

```yaml
networks:
  - soa-network
```

Không cần đóng gói network thành container. Khi chạy Compose, Docker tự tạo network và tự nối các container vào network đó.

### 7.2. Vì sao container gọi nhau bằng tên service?

Docker cung cấp DNS nội bộ cho các container trong cùng network. Vì vậy container có thể gọi container khác bằng tên service:

```text
http://sinhvien-service:8081
http://detai-service:8082
http://dangky-service:8083
```

Docker sẽ tự tìm IP nội bộ của container tương ứng với tên đó.

Ví dụ về mặt khái niệm:

```text
sinhvien-service → 10.x.x.2
detai-service    → 10.x.x.3
api-gateway      → 10.x.x.4
```

Ứng dụng không nên cố định các IP này vì IP container có thể thay đổi khi container được tạo lại. Tên service ổn định hơn IP.

### 7.3. “Mạng nội bộ” nghĩa là gì?

Mạng nội bộ nghĩa là các container trong cùng network có thể gửi request đến nhau bằng hostname và port container. Client chạy bên ngoài không tự động trở thành thành viên của network đó.

Ví dụ Gateway ở trong `soa-network` nên gọi được:

```text
http://sinhvien-service:8081
```

Nhưng Postman chạy trên máy host không nhất thiết gọi được hostname `sinhvien-service`, vì Postman không nằm trong DNS nội bộ của Docker.

---

## 8. Publish port và port nội bộ

Đây là phần dễ nhầm nhất.

### 8.1. Port nội bộ

Một service Spring Boot có thể lắng nghe:

```text
8081
```

Port này tồn tại bên trong container.

### 8.2. Publish port

Cấu hình:

```yaml
ports:
  - "8080:8080"
```

có dạng:

```text
HOST_PORT:CONTAINER_PORT
```

Nó tạo ánh xạ:

```text
Máy host:8080 → Gateway container:8080
```

Client bên ngoài gọi được `localhost:8080` vì đã có đường từ host vào container.

### 8.3. Cấu hình bảo vệ Gateway

Trong cấu hình kiểm thử hiện tại, chỉ Gateway được publish:

```yaml
api-gateway:
  ports:
    - "8080:8080"
```

Ba service nghiệp vụ vẫn dùng các port nội bộ:

```text
sinhvien-service:8081
detai-service:8082
dangky-service:8083
```

nhưng không publish trực tiếp các port đó ra host.

Vì vậy:

```text
Postman → localhost:8080 → Gateway → service nội bộ
```

hoạt động.

Trong khi:

```text
Postman → localhost:8081
Postman → localhost:8082
Postman → localhost:8083
```

không hoạt động từ bên ngoài nếu không có host port mapping.

### 8.4. Điều gì xảy ra nếu biết IP nội bộ của container?

Biết IP nội bộ chưa chắc gọi được container. Client bên ngoài còn cần một đường mạng, route và quyền truy cập vào mạng container đó.

Các container cùng `soa-network` có route nội bộ đến nhau. Máy host hoặc client bên ngoài không mặc nhiên có cùng route như một container trong network.

Có thể hiểu:

- `soa-network` là hành lang nội bộ.
- Publish port là cửa từ bên ngoài vào một container.
- Gateway là container có cửa `8080` được mở ra host.
- Các service nghiệp vụ không có cửa host port nên chỉ tiếp nhận request từ mạng nội bộ.

---

## 9. Ba microservice nghiệp vụ

## 9.1. `sinhvien-service`

Vai trò: quản lý dữ liệu sinh viên.

Port nội bộ:

```text
8081
```

Database:

```text
sinhvien_db
```

Các API chính:

```text
GET  /api/v1/sinhvien
POST /api/v1/sinhvien
GET  /api/v1/sinhvien/{maSv}
```

Service này chịu trách nhiệm:

- Trả danh sách sinh viên.
- Tạo sinh viên mới.
- Tìm sinh viên theo mã sinh viên.
- Trả lỗi nếu không tìm thấy sinh viên.

Service này không có nhiệm vụ lưu đăng ký đề tài.

## 9.2. `detai-service`

Vai trò: quản lý dữ liệu đề tài.

Port nội bộ:

```text
8082
```

Database:

```text
detai_db
```

Các API chính:

```text
GET    /api/v1/detai
GET    /api/v1/detai/{maDeTai}
POST   /api/v1/detai
DELETE /api/v1/detai/{id}
```

Service này chịu trách nhiệm:

- Trả danh sách đề tài.
- Tìm đề tài theo mã.
- Tạo đề tài.
- Xóa đề tài theo ID.

Service này không lưu thông tin đăng ký.

## 9.3. `dangky-service`

Vai trò: quản lý đăng ký đề tài và điều phối nghiệp vụ liên service.

Port nội bộ:

```text
8083
```

Database:

```text
dangky_db
```

Các API chính:

```text
GET    /api/v1/dangky
GET    /api/v1/dangky/{id}
POST   /api/v1/dangky
DELETE /api/v1/dangky/{id}
```

Service này khác hai service còn lại ở chỗ nó cần dữ liệu từ các service khác để hoàn tất nghiệp vụ đăng ký.

---

## 10. Database-per-Service

### 10.1. Khái niệm

Database-per-Service nghĩa là mỗi service sở hữu một database riêng:

```text
sinhvien-service → sinhvien_db
detai-service    → detai_db
dangky-service   → dangky_db
```

Script `init.sql` tạo ba database:

```sql
CREATE DATABASE IF NOT EXISTS sinhvien_db;
CREATE DATABASE IF NOT EXISTS detai_db;
CREATE DATABASE IF NOT EXISTS dangky_db;
```

### 10.2. Vì sao không dùng khóa ngoại giữa các database?

Trong mô hình monolith, có thể có một database chung:

```text
sinhvien
detai
dangky
```

và bảng `dangky` có khóa ngoại đến bảng `sinhvien` hoặc `detai`.

Trong microservices, mỗi service phải sở hữu dữ liệu của mình. `dangky-service` không được truy cập trực tiếp bảng sinh viên hoặc bảng đề tài trong database của service khác.

Do đó, `dangky-service` lưu các mã:

```text
maSv
maDeTai
```

nhưng kiểm tra tính tồn tại bằng cách gọi API:

```text
dangky-service
    → API của sinhvien-service
    → API của detai-service
```

Đây là sự thay đổi từ ràng buộc database sang giao tiếp qua service.

### 10.3. Lợi ích

- Service ít phụ thuộc vào schema database của service khác.
- Mỗi service có thể thay đổi cách lưu trữ riêng.
- Có thể triển khai hoặc mở rộng service độc lập.
- Ranh giới trách nhiệm dữ liệu rõ ràng.

### 10.4. Đánh đổi

- Nghiệp vụ phải thực hiện nhiều HTTP request.
- Có thể phát sinh lỗi mạng hoặc service đích không sẵn sàng.
- Không có transaction database chung bao trùm tất cả service.
- Cần xử lý timeout và lỗi liên service.

---

## 11. OpenFeign và giao tiếp backend-to-backend

### 11.1. OpenFeign là gì?

OpenFeign là thư viện giúp gọi HTTP service-to-service bằng cách khai báo interface Java thay vì tự viết toàn bộ code HTTP client.

Trong `dangky-service` có:

```java
@FeignClient(
    name = "sinhvien-service",
    url = "${services.sinhvien.url:http://localhost:8081}"
)
public interface SinhVienClient {

    @GetMapping("/api/v1/sinhvien/{maSv}")
    ApiResponse<SinhVienDTO> getSinhVienByMaSv(
        @PathVariable("maSv") String maSv
    );
}
```

Interface này mô tả một HTTP request:

```text
GET /api/v1/sinhvien/{maSv}
```

Tương tự, `DeTaiClient` gọi:

```text
GET /api/v1/detai/{maDeTai}
```

### 11.2. URL thay đổi theo môi trường

Khi chạy trực tiếp trên cùng máy:

```text
http://localhost:8081
http://localhost:8082
```

Khi chạy bằng Docker Compose:

```text
http://sinhvien-service:8081
http://detai-service:8082
```

Docker Compose truyền biến môi trường:

```yaml
SERVICES_SINHVIEN_URL=http://sinhvien-service:8081
SERVICES_DETAI_URL=http://detai-service:8082
```

Spring Boot đọc biến môi trường và ghi đè giá trị mặc định trong `application.properties`.

Điểm quan trọng:

```text
localhost trong container = chính container đó
```

Vì vậy `dangky-service` không dùng `localhost:8081` để gọi container sinh viên. Nó dùng tên service `sinhvien-service:8081`.

---

## 12. Luồng tổng quát của một request

### 12.1. Request lấy danh sách sinh viên

Client gửi:

```text
GET http://localhost:8080/api/v1/sinhvien
```

Luồng:

```text
Browser/Postman
    |
    v
Host:8080
    |
    v
api-gateway:8080
    |
    | route theo /api/v1/sinhvien/**
    v
sinhvien-service:8081
    |
    v
sinhvien_db
```

Response quay ngược lại:

```text
sinhvien_db
    → sinhvien-service
    → api-gateway
    → Browser/Postman
```

### 12.2. Request tạo đề tài

Client gửi:

```text
POST http://localhost:8080/api/v1/detai
```

Gateway chuyển đến:

```text
detai-service:8082
```

`detai-service` kiểm tra request, lưu dữ liệu vào `detai_db` và trả response.

### 12.3. Request lấy danh sách đăng ký

Client gửi:

```text
GET http://localhost:8080/api/v1/dangky
```

Gateway chuyển đến:

```text
dangky-service:8083
```

`dangky-service` đọc `dangky_db` và trả danh sách đăng ký.

---

## 13. Luồng đăng ký đề tài end-to-end

Đây là luồng nghiệp vụ quan trọng nhất của dự án.

### 13.1. Dữ liệu request

Client gửi:

```json
{
  "maSv": "SV001",
  "maDeTai": "DT001",
  "ghiChu": "Đăng ký đề tài tốt nghiệp"
}
```

Địa chỉ client gọi:

```text
POST http://localhost:8080/api/v1/dangky
```

### 13.2. Bước 1: Gateway nhận request

Gateway nhìn vào path:

```text
/api/v1/dangky
```

Gateway chọn route `dangky-service` và chuyển request vào:

```text
dangky-service:8083
```

### 13.3. Bước 2: Controller tiếp nhận

`DangKyController` có mapping:

```java
@RequestMapping("/api/v1/dangky")
```

và method:

```java
@PostMapping
```

Controller nhận JSON, chuyển request cho `DangKyService`.

Controller không tự kiểm tra database và không tự gọi các service khác. Nó giao việc cho service layer.

### 13.4. Bước 3: Kiểm tra sinh viên

`DangKyService.register()` gọi:

```java
sinhVienClient.getSinhVienByMaSv(request.getMaSv())
```

Request backend-to-backend:

```text
dangky-service
    → HTTP/OpenFeign
    → http://sinhvien-service:8081/api/v1/sinhvien/SV001
```

Nếu sinh viên tồn tại, `sinhvien-service` trả dữ liệu.

Nếu không tồn tại, service trả lỗi hoặc response không có dữ liệu. `dangky-service` ném lỗi:

```text
Không tìm thấy sinh viên: SV001
```

và không lưu đăng ký.

### 13.5. Bước 4: Kiểm tra đề tài

Nếu sinh viên hợp lệ, `dangky-service` gọi:

```java
deTaiClient.getDeTaiByMaDeTai(request.getMaDeTai())
```

Request:

```text
dangky-service
    → HTTP/OpenFeign
    → http://detai-service:8082/api/v1/detai/DT001
```

Nếu đề tài không tồn tại, service trả lỗi:

```text
Không tìm thấy đề tài: DT001
```

và không lưu bản ghi đăng ký.

### 13.6. Bước 5: Lưu bản ghi

Chỉ khi cả sinh viên và đề tài hợp lệ, `dangky-service` mới tạo entity:

```text
DangKy
├── id
├── maSv
├── maDeTai
├── ngayDangKy
└── ghiChu
```

Entity được lưu vào:

```text
dangky_db
```

`dangky-service` không lưu bản sao toàn bộ thông tin sinh viên hoặc đề tài. Nó lưu mã tham chiếu nghiệp vụ `maSv` và `maDeTai`.

### 13.7. Bước 6: Trả response

Response đi theo chiều ngược lại:

```text
dangky-service
    → API Gateway
    → Client
```

Nếu thành công, controller trả HTTP:

```text
201 Created
```

Nếu dữ liệu không hợp lệ, controller trả:

```text
400 Bad Request
```

### 13.8. Sơ đồ hoàn chỉnh

```text
Client
  |
  | POST /api/v1/dangky
  | { maSv, maDeTai, ghiChu }
  v
API Gateway :8080
  |
  v
dangky-service :8083
  |
  | 1. GET sinhvien-service:8081/api/v1/sinhvien/{maSv}
  v
sinhvien-service
  |
  `-- sinhvien_db

dangky-service :8083
  |
  | 2. GET detai-service:8082/api/v1/detai/{maDeTai}
  v
detai-service
  |
  `-- detai_db

Nếu cả hai hợp lệ:
  dangky-service → dangky_db
  dangky-service → Gateway → Client
```

---

## 14. CORS và trình duyệt

### 14.1. Origin là gì?

Origin gồm:

```text
scheme://host:port
```

Ví dụ:

```text
http://localhost:5500
http://localhost:8080
```

Hai địa chỉ này khác origin vì khác port.

Nếu frontend chạy bằng Live Server ở port `5500` và gọi Gateway ở `8080`, trình duyệt xem đó là cross-origin request.

### 14.2. CORS là gì?

CORS là cơ chế để server thông báo trình duyệt rằng origin nào được phép gọi và đọc response.

Gateway hiện cấu hình cho phép:

- GET
- POST
- PUT
- DELETE
- OPTIONS

và cho phép các origin theo cấu hình hiện tại.

CORS là cơ chế kiểm soát của trình duyệt. Postman hoặc curl thường không bị trình duyệt áp dụng CORS như frontend JavaScript.

### 14.3. CORS không phải authentication

Ba khái niệm cần tách biệt:

```text
CORS:
Trình duyệt có cho JavaScript đọc response không?

Authentication:
Request có xác thực người dùng hay không?

Authorization:
Người dùng đã xác thực có quyền thực hiện hành động hay không?
```

Dự án hiện tại mới cấu hình CORS ở Gateway, chưa triển khai authentication và authorization.

---

## 15. Database và MySQL trong Docker Compose

MySQL chạy trong một container riêng:

```text
mysql
```

MySQL dùng volume:

```yaml
volumes:
  - mysql-data:/var/lib/mysql
```

Volume giúp dữ liệu tồn tại độc lập với vòng đời container. Nếu container MySQL bị recreate nhưng volume không bị xóa, dữ liệu vẫn có thể được giữ lại.

Compose dùng healthcheck:

```yaml
healthcheck:
  test: ["CMD", "mysqladmin", "ping", ...]
```

Các service nghiệp vụ phụ thuộc MySQL ở trạng thái healthy:

```yaml
depends_on:
  mysql:
    condition: service_healthy
```

Mục đích là tránh để Spring Boot khởi động trước khi MySQL sẵn sàng.

Trong container, service kết nối database bằng hostname:

```text
mysql:3306
```

Không dùng `localhost:3306` khi service nằm trong container, vì `localhost` khi đó trỏ về chính container ứng dụng.

---

## 16. Chạy hệ thống

### 16.1. Build các file JAR

Ở thư mục gốc:

```bash
./mvnw clean package -DskipTests
```

Trên Windows có thể dùng:

```cmd
mvnw.cmd clean package -DskipTests
```

Mỗi Dockerfile copy file JAR từ thư mục `target` vào image.

### 16.2. Khởi động Compose

```bash
docker compose up -d
```

Nếu source Java hoặc Dockerfile đã thay đổi:

```bash
docker compose up -d --build
```

Nếu chỉ thay đổi `docker-compose.yml`, thông thường không cần build image:

```bash
docker compose up -d
```

Nếu Compose cần tạo lại container theo cấu hình mới:

```bash
docker compose up -d --force-recreate
```

Không nên tùy tiện dùng:

```bash
docker compose down -v
```

vì `-v` có thể xóa volume database.

### 16.3. Kiểm tra trạng thái

```bash
docker compose ps
```

Hoặc nếu môi trường dùng Podman Compose, lệnh `docker compose` có thể được chuyển tiếp sang `podman-compose`. Khi đó vẫn cần kiểm tra danh sách container bằng công cụ container đang được hệ thống sử dụng.

### 16.4. Địa chỉ truy cập chính

Client nên gọi:

```text
http://localhost:8080
```

Frontend hiện tại dùng:

```text
http://localhost:8080/api/v1
```

---

## 17. Kiểm thử qua Gateway

### 17.1. Tạo sinh viên

```text
POST http://localhost:8080/api/v1/sinhvien
```

Ví dụ body:

```json
{
  "maSv": "SV001",
  "hoTen": "Nguyen Van A",
  "chuyenNganh": "Ky thuat phan mem",
  "gpa": 3.5
}
```

### 17.2. Tạo đề tài

```text
POST http://localhost:8080/api/v1/detai
```

Ví dụ body:

```json
{
  "maDeTai": "DT001",
  "tenDeTai": "Xay dung he thong SOA",
  "moTa": "Nghien cuu kien truc huong dich vu",
  "gvhd": "Giang vien A"
}
```

### 17.3. Đăng ký đề tài

```text
POST http://localhost:8080/api/v1/dangky
```

Body:

```json
{
  "maSv": "SV001",
  "maDeTai": "DT001",
  "ghiChu": "Dang ky de tai tot nghiep"
}
```

### 17.4. Xem danh sách đăng ký

```text
GET http://localhost:8080/api/v1/dangky
```

### 17.5. Kiểm thử lỗi

Nếu gửi mã sinh viên không tồn tại:

```json
{
  "maSv": "SV999",
  "maDeTai": "DT001",
  "ghiChu": "Du lieu sai"
}
```

`dangky-service` gọi `sinhvien-service`, nhận lỗi không tìm thấy sinh viên và trả về `400 Bad Request`. Bản ghi không được lưu vào `dangky_db`.

---

## 18. Phân biệt các loại giao tiếp

### Client-to-backend

```text
Browser/Postman → API Gateway
```

Đây là request từ bên ngoài hệ thống vào backend.

### Backend-to-backend

```text
API Gateway → microservice
dangky-service → sinhvien-service
dangky-service → detai-service
```

Đây là giao tiếp giữa các ứng dụng backend bằng HTTP.

### Container-to-container

Đây là cách nhìn ở tầng triển khai:

```text
Gateway container → Service container
```

Request backend-to-backend được truyền qua Docker network ở tầng container.

Không nên nhầm:

- Docker network không thay thế HTTP.
- Docker network cung cấp đường kết nối và DNS nội bộ.
- HTTP/OpenFeign là cách ứng dụng gửi request trên đường kết nối đó.

---

## 19. Những điều dự án hiện tại chưa triển khai

Để thuyết trình chính xác, không nên nói dự án đã có các tính năng sau nếu code chưa có:

- Đăng nhập người dùng.
- JWT.
- Spring Security.
- Session login.
- Phân quyền sinh viên, giảng viên hoặc admin.
- Service discovery như Eureka.
- Circuit breaker như Resilience4j.
- Message broker như Kafka hoặc RabbitMQ.
- Distributed tracing.

Dự án hiện tại có:

- Spring Boot.
- Spring Cloud Gateway.
- OpenFeign.
- Docker Compose.
- Docker bridge network.
- Database-per-Service.
- MySQL.
- REST API.
- Frontend HTML/JavaScript gọi Gateway.

---

## 20. Cách trả lời các câu hỏi thường gặp

### API Gateway có phải thư viện bảo mật không?

Không. Spring Cloud Gateway là framework dùng để xây dựng cổng trung gian và định tuyến request. Nó có thể tích hợp thêm security, nhưng trong dự án hiện tại Gateway chủ yếu route request và cấu hình CORS.

### Vì sao client không gọi trực tiếp service?

Vì các port `8081`, `8082`, `8083` không được publish ra host. Các service chỉ tham gia Docker network nội bộ. Client bên ngoài chỉ có cửa vào được publish là port `8080` của Gateway.

### Service có còn port 8081, 8082, 8083 không?

Có. Đó là port ứng dụng bên trong container. Không publish ra host không có nghĩa là service mất port; chỉ có nghĩa là client bên ngoài không có ánh xạ trực tiếp vào port đó.

### `soa-network` là gì?

Đó là mạng ảo do Docker Compose tạo ra để các container giao tiếp. Nó không phải container. Các container trong cùng network có thể gọi nhau bằng tên service nhờ DNS nội bộ.

### Nếu biết IP nội bộ của container thì có gọi được không?

Không nhất thiết. IP đó thuộc mạng container nội bộ. Client bên ngoài cần có route và đường truy cập vào mạng đó. Với cấu hình thông thường, client nên gọi Gateway qua port đã publish thay vì gọi IP nội bộ.

### Không dùng Docker thì service gọi nhau được không?

Có. Nếu chạy cùng máy, service có thể gọi nhau bằng `localhost` và port khác nhau. Nếu chạy trên các máy khác nhau, phải dùng IP hoặc hostname của máy chứa service. Docker chỉ thay đổi cách đóng gói và cách phân giải địa chỉ trong môi trường container.

### Tại sao `dangky-service` không truy cập trực tiếp database của service khác?

Vì hệ thống dùng Database-per-Service. Mỗi service sở hữu database riêng. `dangky-service` phải gọi API của `sinhvien-service` và `detai-service` để kiểm tra dữ liệu, thay vì truy cập trực tiếp bảng của database khác.

### Nếu Gateway bị tắt thì sao?

Client không truy cập được hệ thống qua port `8080`, dù các service phía sau có thể vẫn đang chạy. Đây là đánh đổi của việc dùng một cổng vào tập trung. Các service vẫn có thể giao tiếp nội bộ nếu chúng còn hoạt động.

### Nếu `sinhvien-service` bị tắt thì đăng ký có được không?

Không. `dangky-service` cần gọi `sinhvien-service` để xác thực sinh viên. Nếu service không phản hồi, nghiệp vụ đăng ký không thể hoàn tất.

---

## 21. Kịch bản thuyết trình tổng quan

Có thể trình bày như sau:

> Em xin phép giới thiệu tổng quan về hệ thống quản lý đồ án tốt nghiệp. Hệ thống được xây dựng theo kiến trúc hướng dịch vụ SOA và được triển khai theo mô hình microservices. Thay vì đưa toàn bộ chức năng vào một ứng dụng duy nhất, hệ thống được chia thành các service độc lập theo từng nghiệp vụ.
>
> Hệ thống gồm ba service nghiệp vụ chính. `sinhvien-service` quản lý thông tin sinh viên, `detai-service` quản lý danh sách đề tài và `dangky-service` xử lý nghiệp vụ đăng ký đề tài. Bên ngoài các service này có thêm API Gateway, được xây dựng bằng Spring Cloud Gateway.
>
> API Gateway đóng vai trò là cổng tiếp nhận request tập trung. Client chỉ giao tiếp với hệ thống qua port 8080. Dựa vào path của URL, Gateway định tuyến request đến đúng service. Ví dụ path `/api/v1/sinhvien` được chuyển đến `sinhvien-service`, path `/api/v1/detai` được chuyển đến `detai-service`, còn path `/api/v1/dangky` được chuyển đến `dangky-service`.
>
> Các thành phần được đóng gói bằng Docker. Mỗi service chạy trong một container riêng và cùng tham gia mạng nội bộ `soa-network`. Đây là một Docker network, không phải một container. Nó giúp các container giao tiếp với nhau bằng tên service, chẳng hạn Gateway gọi `sinhvien-service:8081`.
>
> Các service nghiệp vụ có port riêng trong container, nhưng không publish trực tiếp ra máy host. Vì vậy client bên ngoài không thể gọi trực tiếp vào các service mà phải đi qua Gateway tại port 8080. Gateway nhận request từ bên ngoài rồi chuyển tiếp request qua mạng nội bộ đến service tương ứng.
>
> Về dữ liệu, hệ thống sử dụng mô hình Database-per-Service. `sinhvien-service` sử dụng `sinhvien_db`, `detai-service` sử dụng `detai_db`, còn `dangky-service` sử dụng `dangky_db`. Các database không liên kết trực tiếp bằng khóa ngoại giữa các service. Khi cần kiểm tra dữ liệu thuộc service khác, hệ thống gọi API của service đó.
>
> Trong phần nghiệp vụ đăng ký, `dangky-service` đóng vai trò điều phối. Khi client gửi yêu cầu đăng ký, request đi qua Gateway đến `dangky-service`. Service này gọi sang `sinhvien-service` bằng HTTP/OpenFeign để kiểm tra sinh viên, sau đó gọi sang `detai-service` để kiểm tra đề tài. Chỉ khi cả hai dữ liệu hợp lệ, `dangky-service` mới lưu bản ghi vào `dangky_db` và trả response về cho client thông qua Gateway.

---

## 22. Kịch bản thuyết trình riêng về `dangky-service`

> `dangky-service` là service phụ trách quản lý các lượt đăng ký đề tài. Service cung cấp các API để xem danh sách đăng ký, xem chi tiết một đăng ký, tạo đăng ký mới và xóa đăng ký.
>
> Điểm đặc biệt của `dangky-service` là nghiệp vụ đăng ký phụ thuộc vào dữ liệu do hai service khác quản lý. Vì vậy service không chỉ lưu dữ liệu vào database của mình mà còn đóng vai trò điều phối liên service.
>
> Khi nhận request tạo đăng ký, `dangky-service` trước tiên gọi `sinhvien-service` thông qua OpenFeign để kiểm tra mã sinh viên. Nếu sinh viên không tồn tại, service trả lỗi và dừng xử lý. Nếu sinh viên hợp lệ, service tiếp tục gọi `detai-service` để kiểm tra mã đề tài.
>
> Khi cả sinh viên và đề tài đều tồn tại, `dangky-service` tạo một bản ghi đăng ký gồm mã sinh viên, mã đề tài, thời gian đăng ký và ghi chú. Bản ghi được lưu vào `dangky_db`. Service không truy cập trực tiếp vào database của sinh viên hoặc đề tài.
>
> Sau khi lưu thành công, `dangky-service` trả response HTTP 201 Created. Response đi ngược qua API Gateway về client. Nếu dữ liệu không hợp lệ, service trả HTTP 400 Bad Request và không tạo bản ghi.
>
> Vì vậy, `dangky-service` thể hiện rõ cách các service phối hợp trong kiến trúc SOA: mỗi service giữ dữ liệu của mình, còn nghiệp vụ liên quan nhiều service được thực hiện thông qua API và giao tiếp HTTP.

---

## 23. Tóm tắt một câu cho từng thành phần

```text
Frontend:
Giao diện người dùng, gửi request đến Gateway.

API Gateway:
Cổng vào duy nhất, định tuyến request theo URL.

sinhvien-service:
Quản lý sinh viên và sinhvien_db.

detai-service:
Quản lý đề tài và detai_db.

dangky-service:
Quản lý đăng ký, gọi các service khác để xác thực nghiệp vụ.

OpenFeign:
Giúp dangky-service gọi API của sinhvien-service và detai-service.

soa-network:
Mạng nội bộ nối các container và cung cấp DNS nội bộ.

Docker Compose:
Mô tả và khởi động toàn bộ container, network, volume và biến môi trường.

Database-per-Service:
Mỗi service sở hữu một database riêng.
```

---

## 24. Câu kết luận về kiến trúc

> Toàn bộ hệ thống được tổ chức theo nguyên tắc mỗi service chịu trách nhiệm một nghiệp vụ và sở hữu dữ liệu của mình. API Gateway tạo ra một điểm truy cập tập trung cho client, Docker Compose tạo môi trường triển khai tách biệt cho từng service, còn `soa-network` cho phép các container giao tiếp nội bộ. `dangky-service` là ví dụ rõ nhất về phối hợp liên service: nó không truy cập trực tiếp database của service khác mà gọi API qua HTTP/OpenFeign để xác thực dữ liệu trước khi lưu đăng ký vào database riêng.

