# Hướng dẫn xem Database trong Docker

## 1. Khởi động hệ thống

Tại thư mục gốc dự án:

```bash
docker compose up -d
docker compose ps
```

Container `soa-mysql` cần có trạng thái đang chạy.

## 2. Kết nối từ DBeaver hoặc MySQL Workbench

Tạo kết nối với:

```text
Host:     localhost
Port:     3307
Username: root
Password: root
```

Docker chuyển tiếp:

```text
localhost:3307 -> MySQL trong container:3306
```

## 3. Xem các database và bảng

Chạy các lệnh SQL:

```sql
SHOW DATABASES;

USE sinhvien_db;
SHOW TABLES;
SELECT * FROM SINHVIEN;
```

Các database của dự án:

```text
sinhvien_db  -> bảng SINHVIEN
detai_db     -> bảng DETAI
dangky_db    -> bảng DANGKY
```

Ví dụ xem đề tài:

```sql
USE detai_db;
SELECT * FROM DETAI;
```

Ví dụ xem đăng ký:

```sql
USE dangky_db;
SELECT * FROM DANGKY;
```

## 4. Xem bằng terminal

```bash
docker exec -it soa-mysql mysql -uroot -proot
```

Sau đó chạy các lệnh SQL ở trên. Thoát bằng:

```sql
exit;
```

## 5. Lưu ý về port

```text
Backend container -> mysql:3306
Máy thật/DBeaver   -> localhost:3307
```

`3306` là port MySQL bên trong Docker. `3307` là port được Docker mở ra cho máy thật.
