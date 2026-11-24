-- ========================================================
-- HỆ THỐNG QUẢN LÝ ĐỀ TÀI NCKH THEO KIẾN TRÚC SOA
-- Khởi tạo 8 Cơ sở dữ liệu độc lập (Database-per-Service)
-- ========================================================

-- 1. AUTH SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS auth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE auth_db;

CREATE TABLE IF NOT EXISTS tai_khoan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_dang_nhap VARCHAR(50) UNIQUE NOT NULL,
    mat_khau VARCHAR(255) NOT NULL,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    vai_tro VARCHAR(30) NOT NULL, -- ADMIN, SINH_VIEN, GIANG_VIEN, QUAN_LY
    ma_dinh_danh VARCHAR(50),
    trang_thai VARCHAR(20) DEFAULT 'HOAT_DONG',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Mật khẩu mặc định: admin123 / 123456 (BCrypt)
INSERT INTO tai_khoan (ten_dang_nhap, mat_khau, ho_ten, email, vai_tro, ma_dinh_danh) VALUES
('admin', '$2a$10$7Z8lqYlO9z.1o.g1V6U36.G5Dk2Z1jK9f7VlT1w9P6W8G7F5D4C3B', 'Quản trị viên Hệ thống', 'admin@qnu.edu.vn', 'ADMIN', 'ADMIN01'),
('quanly', '$2a$10$7Z8lqYlO9z.1o.g1V6U36.G5Dk2Z1jK9f7VlT1w9P6W8G7F5D4C3B', 'Cán bộ Quản lý NCKH', 'qlkh@qnu.edu.vn', 'QUAN_LY', 'QL01'),
('gv_hung', '$2a$10$7Z8lqYlO9z.1o.g1V6U36.G5Dk2Z1jK9f7VlT1w9P6W8G7F5D4C3B', 'TS. Nguyễn Văn Hùng', 'nvhung@qnu.edu.vn', 'GIANG_VIEN', 'GV001'),
('gv_lan', '$2a$10$7Z8lqYlO9z.1o.g1V6U36.G5Dk2Z1jK9f7VlT1w9P6W8G7F5D4C3B', 'PGS.TS. Trần Thị Lan', 'ttlan@qnu.edu.vn', 'GIANG_VIEN', 'GV002'),
('sv_loc', '$2a$10$7Z8lqYlO9z.1o.g1V6U36.G5Dk2Z1jK9f7VlT1w9P6W8G7F5D4C3B', 'Võ Đại Lộc', 'vodailoc@gmail.com', 'SINH_VIEN', 'SV001'),
('sv_khoa', '$2a$10$7Z8lqYlO9z.1o.g1V6U36.G5Dk2Z1jK9f7VlT1w9P6W8G7F5D4C3B', 'Quách Duy Khoa', 'khoa@gmail.com', 'SINH_VIEN', 'SV002')
ON DUPLICATE KEY UPDATE id=id;

-- 2. USER SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS user_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE user_db;

CREATE TABLE IF NOT EXISTS khoa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_khoa VARCHAR(30) UNIQUE NOT NULL,
    ten_khoa VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS lop (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_lop VARCHAR(30) UNIQUE NOT NULL,
    ten_lop VARCHAR(100) NOT NULL,
    ma_khoa VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS sinhvien (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_sv VARCHAR(30) UNIQUE NOT NULL,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    so_dien_thoai VARCHAR(20),
    chuyen_nganh VARCHAR(100),
    khoa VARCHAR(100),
    lop VARCHAR(50),
    gpa DOUBLE DEFAULT 3.2,
    trang_thai VARCHAR(30) DEFAULT 'DANG_HOC'
);

CREATE TABLE IF NOT EXISTS giangvien (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_gv VARCHAR(30) UNIQUE NOT NULL,
    ho_ten VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    so_dien_thoai VARCHAR(20),
    hoc_vi VARCHAR(50),
    khoa VARCHAR(100),
    bo_mon VARCHAR(100),
    huong_nghien_cuu TEXT
);

INSERT INTO khoa (ma_khoa, ten_khoa) VALUES 
('CNTT', 'Khoa Công nghệ Thông tin'),
('TOAN', 'Khoa Toán - Thống kê'),
('KT', 'Khoa Kinh tế & Kế toán')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO lop (ma_lop, ten_lop, ma_khoa) VALUES 
('KTPM_K46', 'Kỹ thuật phần mềm K46', 'CNTT'),
('KHMT_K46', 'Khoa học máy tính K46', 'CNTT')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO sinhvien (ma_sv, ho_ten, email, so_dien_thoai, chuyen_nganh, khoa, lop, gpa, trang_thai) VALUES
('SV001', 'Võ Đại Lộc', 'vodailoc@gmail.com', '0912345678', 'Kỹ thuật phần mềm', 'Công nghệ Thông tin', 'KTPM_K46', 3.55, 'DANG_HOC'),
('SV002', 'Quách Duy Khoa', 'khoa@gmail.com', '0912345679', 'Kỹ thuật phần mềm', 'Công nghệ Thông tin', 'KTPM_K46', 3.60, 'DANG_HOC'),
('SV003', 'Hồ Đoàn Chiến', 'chien@gmail.com', '0912345680', 'Kỹ thuật phần mềm', 'Công nghệ Thông tin', 'KTPM_K46', 3.40, 'DANG_HOC')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO giangvien (ma_gv, ho_ten, email, so_dien_thoai, hoc_vi, khoa, bo_mon, huong_nghien_cuu) VALUES
('GV001', 'TS. Nguyễn Văn Hùng', 'nvhung@qnu.edu.vn', '0988111222', 'Tiến sĩ', 'Công nghệ Thông tin', 'Kỹ thuật phần mềm', 'Kiến trúc SOA, Microservices, Cloud Computing'),
('GV002', 'PGS.TS. Trần Thị Lan', 'ttlan@qnu.edu.vn', '0988333444', 'Phó Giáo sư', 'Công nghệ Thông tin', 'Khoa học máy tính', 'Trí tuệ nhân tạo, Xử lý ảnh, Deep Learning')
ON DUPLICATE KEY UPDATE id=id;

-- 3. DETAI SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS detai_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE detai_db;

CREATE TABLE IF NOT EXISTS detai (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_detai VARCHAR(30) UNIQUE NOT NULL,
    ten_de_tai VARCHAR(255) NOT NULL,
    mo_ta TEXT,
    linh_vuc VARCHAR(100),
    kinh_phi_du_kien DOUBLE DEFAULT 15000000,
    ma_sv_chu_nhiem VARCHAR(30),
    ma_gvhd VARCHAR(30),
    ten_gvhd VARCHAR(100),
    so_luong_hien_tai INT DEFAULT 1,
    so_luong_toi_da INT DEFAULT 3,
    trang_thai VARCHAR(50) DEFAULT 'CHO_DUYET', -- CHO_DUYET, DA_DUYET, DANG_THUC_HIEN, CHO_NGHIEM_THU, DA_NGHIEM_THU, DA_HUY
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ngay_cap_nhat TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dangky (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_detai VARCHAR(30) NOT NULL,
    ma_sv VARCHAR(30) NOT NULL,
    vai_tro_thanh_vien VARCHAR(50) DEFAULT 'THANH_VIEN',
    trang_thai VARCHAR(30) DEFAULT 'XAC_NHAN',
    ngay_dang_ky TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_sv_detai (ma_sv, ma_detai)
);

INSERT INTO detai (ma_detai, ten_de_tai, mo_ta, linh_vuc, kinh_phi_du_kien, ma_sv_chu_nhiem, ma_gvhd, ten_gvhd, so_luong_hien_tai, so_luong_toi_da, trang_thai) VALUES
('DT01', 'Nghiên cứu ứng dụng IoT và Kiến trúc Hướng Dịch vụ trong Nông nghiệp thông minh', 'Xây dựng mô hình giám sát nhiệt ẩm đất tự động bằng Microservices', 'Công nghệ phần mềm & IoT', 20000000, 'SV001', 'GV001', 'TS. Nguyễn Văn Hùng', 2, 3, 'DA_DUYET'),
('DT02', 'Phát triển mô hình Deep Learning phân loại bệnh cây trồng', 'Ứng dụng mạng nơ-ron tích chập (CNN) phát hiện sâu bệnh trên lá lúa', 'Trí tuệ nhân tạo', 18000000, 'SV002', 'GV002', 'PGS.TS. Trần Thị Lan', 1, 3, 'CHO_DUYET'),
('DT03', 'Hệ thống Quản lý Đề tài NCKH cấp Trường theo chuẩn SOA và Spring Cloud', 'Xây dựng giải pháp số hóa toàn diện quy trình NCKH trường đại học', 'Công nghệ thông tin', 25000000, 'SV003', 'GV001', 'TS. Nguyễn Văn Hùng', 1, 3, 'DANG_THUC_HIEN')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO dangky (ma_detai, ma_sv, vai_tro_thanh_vien, trang_thai) VALUES
('DT01', 'SV001', 'CHU_NHIEM', 'XAC_NHAN'),
('DT01', 'SV002', 'THANH_VIEN', 'XAC_NHAN'),
('DT02', 'SV002', 'CHU_NHIEM', 'XAC_NHAN'),
('DT03', 'SV003', 'CHU_NHIEM', 'XAC_NHAN')
ON DUPLICATE KEY UPDATE id=id;

-- 4. COUNCIL SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS council_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE council_db;

CREATE TABLE IF NOT EXISTS hoi_dong (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_hoidong VARCHAR(30) UNIQUE NOT NULL,
    ten_hoi_dong VARCHAR(255) NOT NULL,
    loai_hoi_dong VARCHAR(50) NOT NULL, -- DE_CUONG, BAO_VE_CHINH_THUC
    ngay_hop DATE,
    dia_diem VARCHAR(100),
    trang_thai VARCHAR(30) DEFAULT 'DANG_HOAT_DONG'
);

CREATE TABLE IF NOT EXISTS thanh_vien_hoi_dong (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_hoidong VARCHAR(30) NOT NULL,
    ma_gv VARCHAR(30) NOT NULL,
    ten_gv VARCHAR(100),
    vai_tro VARCHAR(50) NOT NULL -- CHU_TICH, THU_KY, PHAN_BIEN_1, PHAN_BIEN_2, UY_VIEN
);

CREATE TABLE IF NOT EXISTS phan_cong_de_tai (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_hoidong VARCHAR(30) NOT NULL,
    ma_detai VARCHAR(30) NOT NULL,
    ten_detai VARCHAR(255),
    ngay_phan_cong TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO hoi_dong (ma_hoidong, ten_hoi_dong, loai_hoi_dong, ngay_hop, dia_diem, trang_thai) VALUES
('HD01', 'Hội đồng Thẩm định Đề cương NCKH Khoa CNTT 2026', 'DE_CUONG', '2026-11-30', 'Phòng Hội thảo A2', 'DANG_HOAT_DONG'),
('HD02', 'Hội đồng Nghiệm thu Đề tài NCKH Cấp Cơ sở Đợt 1', 'BAO_VE_CHINH_THUC', '2027-01-15', 'Phòng Chuyên đề B1', 'DANG_HOAT_DONG')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO thanh_vien_hoi_dong (ma_hoidong, ma_gv, ten_gv, vai_tro) VALUES
('HD01', 'GV001', 'TS. Nguyễn Văn Hùng', 'CHU_TICH'),
('HD01', 'GV002', 'PGS.TS. Trần Thị Lan', 'PHAN_BIEN_1')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO phan_cong_de_tai (ma_hoidong, ma_detai, ten_detai) VALUES
('HD01', 'DT01', 'Nghiên cứu ứng dụng IoT và Kiến trúc Hướng Dịch vụ trong Nông nghiệp thông minh'),
('HD01', 'DT03', 'Hệ thống Quản lý Đề tài NCKH cấp Trường theo chuẩn SOA và Spring Cloud')
ON DUPLICATE KEY UPDATE id=id;

-- 5. GRADING SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS grading_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE grading_db;

CREATE TABLE IF NOT EXISTS diem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_detai VARCHAR(30) NOT NULL,
    ma_gv VARCHAR(30) NOT NULL,
    ten_gv VARCHAR(100),
    loai_diem VARCHAR(50) NOT NULL, -- DE_CUONG, BAO_VE
    diem_so DOUBLE NOT NULL,
    nhan_xet TEXT,
    ngay_cham TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO diem (ma_detai, ma_gv, ten_gv, loai_diem, diem_so, nhan_xet) VALUES
('DT01', 'GV001', 'TS. Nguyễn Văn Hùng', 'DE_CUONG', 8.5, 'Ý tưởng thuyết minh rõ ràng, tính ứng dụng thực tiễn cao.'),
('DT01', 'GV002', 'PGS.TS. Trần Thị Lan', 'DE_CUONG', 9.0, 'Đề tài bám sát chuyên môn, kế hoạch phân bổ thời gian hợp lý.')
ON DUPLICATE KEY UPDATE id=id;

-- 6. ANALYTICS SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS analytics_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE analytics_db;

CREATE TABLE IF NOT EXISTS so_lieu_tong_hop (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ky_bao_cao VARCHAR(50) NOT NULL,
    tong_so_de_tai INT DEFAULT 0,
    so_de_tai_dat INT DEFAULT 0,
    so_de_tai_khong_dat INT DEFAULT 0,
    so_de_tai_dang_thuc_hien INT DEFAULT 0,
    ngay_tong_hop TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO so_lieu_tong_hop (ky_bao_cao, tong_so_de_tai, so_de_tai_dat, so_de_tai_khong_dat, so_de_tai_dang_thuc_hien) VALUES
('Học kỳ 1 Năm học 2026-2027', 3, 1, 0, 2)
ON DUPLICATE KEY UPDATE id=id;

-- 7. NOTIFICATION SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS notification_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE notification_db;

CREATE TABLE IF NOT EXISTS thong_bao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_nguoi_nhan VARCHAR(50) NOT NULL,
    tieu_de VARCHAR(255) NOT NULL,
    noi_dung TEXT NOT NULL,
    loai_thong_bao VARCHAR(50) DEFAULT 'HE_THONG',
    da_doc BOOLEAN DEFAULT FALSE,
    ngay_gui TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO thong_bao (ma_nguoi_nhan, tieu_de, noi_dung) VALUES
('SV001', 'Phê duyệt đề cương đề tài DT01', 'Đề tài của bạn đã được Hội đồng và Phòng QLKH phê duyệt.'),
('SV002', 'Đăng ký đề tài DT02 thành công', 'Hồ sơ thuyết minh đề tài DT02 đang chờ Phòng QLKH thẩm định.')
ON DUPLICATE KEY UPDATE id=id;

-- 8. STORAGE SERVICE DATABASE
CREATE DATABASE IF NOT EXISTS storage_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE storage_db;

CREATE TABLE IF NOT EXISTS tai_lieu (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ma_detai VARCHAR(30) NOT NULL,
    ten_file VARCHAR(255) NOT NULL,
    duong_dan VARCHAR(500) NOT NULL,
    loai_tai_lieu VARCHAR(50), -- THUYET_MINH, BAO_CAO_TIEN_DO, BAO_CAO_NGHIEM_THU
    kich_thuoc BIGINT,
    nguoi_upload VARCHAR(50),
    ngay_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tai_lieu (ma_detai, ten_file, duong_dan, loai_tai_lieu, kich_thuoc, nguoi_upload) VALUES
('DT01', 'Thuyet_Minh_De_Tai_DT01.pdf', '/uploads/DT01/Thuyet_Minh_De_Tai_DT01.pdf', 'THUYET_MINH', 1048576, 'SV001')
ON DUPLICATE KEY UPDATE id=id;
