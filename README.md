# Hướng dẫn Khởi chạy Dự án (Windows)

### 1. Yêu cầu môi trường
- Cài đặt **Java / JDK 21** trở lên.
- Kiểm tra phiên bản Java trên máy bằng lệnh:
  ```cmd
  java -version
  ```
> 💡 *Không cần cài đặt Apache Maven vì dự án đã tích hợp sẵn Maven Wrapper.*

---

### 2. Các bước khởi chạy

1. Mở thư mục dự án trên Windows bằng **PowerShell** hoặc **Command Prompt (CMD)**:
   - *Cách nhanh:* Mở thư mục dự án trong File Explorer, nhấp chuột vào thanh địa chỉ, gõ `cmd` hoặc `powershell` rồi nhấn **Enter**.

2. Chạy lệnh khởi động server:
   - **Trên PowerShell**:
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```
   - **Trên Command Prompt (CMD)**:
     ```cmd
     mvnw.cmd spring-boot:run
     ```

3. Khi màn hình terminal xuất hiện thông báo:
   ```text
   Started SoaApplication in ... seconds
   ```
   tức là ứng dụng đã khởi động thành công trên cổng mặc định `8080`.

---

### 3. Kiểm tra kết quả (API Testing)

#### Cách 1 — Kiểm tra trên Trình duyệt Web
Mở trình duyệt bất kỳ (Chrome, Edge) và truy cập:
- `http://localhost:8080/hello` $\rightarrow$ Kết quả: `Hello World`
- `http://localhost:8080/hello?name=Loc` $\rightarrow$ Kết quả: `Hello Loc`
- `http://localhost:8080/swagger-ui/index.html` $\rightarrow$ Giao diện tài liệu Swagger UI.

#### Cách 2 — Kiểm tra bằng Postman
1. Mở ứng dụng **Postman**, bấm nút **Import** ở góc trên bên trái.
2. Chọn file collection có sẵn trong thư mục dự án: `postman/SOA_HelloWorld.postman_collection.json`.
3. Chọn request mẫu và bấm nút **Send** để nhận phản hồi `200 OK` cùng nội dung `Hello World`.

---

### 4. Dừng ứng dụng
- Tại cửa sổ terminal đang chạy server, nhấn tổ hợp phím **`Ctrl + C`** để tắt ứng dụng.
