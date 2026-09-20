# Hướng dẫn Khởi chạy Dự án (Windows)

### 1. Yêu cầu môi trường
- Cài đặt **Java / JDK 21** trở lên.
- Kiểm tra phiên bản Java trên máy bằng lệnh:
  ```cmd
  java -version
  ```
> 💡 *Không cần cài đặt Apache Maven vì dự án đã tích hợp sẵn Maven Wrapper.*

---

### 2. Các bước khởi chạy dự án

#### Cách 1 — Chạy trong Terminal của VS Code (Khuyên dùng)
1. Mở thư mục dự án bằng **VS Code** (`File` → `Open Folder...`).
2. Mở Terminal tích hợp trong VS Code:
   - Nhấn tổ hợp phím: **`Ctrl + ` `** *(phím Ctrl kết hợp phím dấu ngã góc trên bên trái bàn phím)*.
   - Hoặc chọn trên thanh menu: **`Terminal`** → **`New Terminal`**.
3. Dán lệnh sau vào Terminal và nhấn **Enter**:
   ```bash
   .\mvnw.cmd spring-boot:run
   ```

#### Cách 2 — Chạy bằng Command Prompt (CMD) của Windows
1. Mở thư mục dự án trong File Explorer, nhấp chuột vào thanh địa chỉ thư mục, gõ `cmd` rồi nhấn **Enter**.
2. Dán lệnh sau vào CMD và nhấn **Enter**:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

---

### 3. Kiểm tra kết quả

Khi terminal xuất hiện dòng chữ:
```text
Started SoaApplication in ... seconds
```
tức là server đã khởi động thành công trên cổng mặc định `8080`.

#### Kiểm tra trên Trình duyệt Web
Mở trình duyệt (Chrome, Edge) và truy cập:
- `http://localhost:8080/hello` → Kết quả hiển thị: `Hello World`
- `http://localhost:8080/hello?name=Loc` → Kết quả hiển thị: `Hello Loc`
- `http://localhost:8080/swagger-ui/index.html` → Xem tài liệu API Swagger UI.

#### Kiểm tra bằng Postman
1. Mở ứng dụng **Postman**, bấm nút **Import** ở góc trên bên trái.
2. Chọn file collection có sẵn trong thư mục dự án: `postman/SOA_HelloWorld.postman_collection.json`.
3. Bấm **Send** để kiểm tra API trả về mã `200 OK` cùng nội dung `Hello World`.

---

### 4. Dừng ứng dụng
- Tại cửa sổ Terminal đang chạy server, nhấn tổ hợp phím **`Ctrl + C`** để tắt ứng dụng.
