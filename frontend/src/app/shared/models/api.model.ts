export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  tenDangNhap: string;
  hoTen: string;
  vaiTro: string;
  maDinhDanh: string;
  expiresIn: number;
}

export interface LoginRequest {
  tenDangNhap: string;
  matKhau: string;
}

export interface RegisterRequest {
  tenDangNhap: string;
  matKhau: string;
  hoTen: string;
  email: string;
  maSinhVien: string;
}

export interface SinhVien {
  maSv: string;
  hoTen: string;
  email: string;
  soDienThoai: string;
  chuyenNganh: string;
  khoa: string;
  lop: string;
  gpa: number;
  trangThai: string;
}

export interface GiangVien {
  maGv: string;
  hoTen: string;
  email: string;
  soDienThoai: string;
  hocVi: string;
  khoa: string;
  boMon: string;
  huongNghienCuu: string;
}

export interface Khoa {
  maKhoa: string;
  tenKhoa: string;
}

export interface Lop {
  maLop: string;
  tenLop: string;
  maKhoa: string;
}

export interface DeTai {
  maDeTai: string;
  tenDeTai: string;
  moTa: string;
  linhVuc: string;
  kinhPhiDuKien: number;
  maSvChuNhiem: string;
  maGvhd: string;
  tenGvhd: string;
  soLuongHienTai: number;
  soLuongToiDa: number;
  trangThai: string;
  ngayTao: string;
}

export interface TaoDeTaiRequest {
  maDeTai: string;
  tenDeTai: string;
  moTa: string;
  linhVuc: string;
  kinhPhiDuKien: number;
  maSvChuNhiem: string;
  maGvhd: string;
  tenGvhd: string;
  soLuongToiDa: number;
}

export interface DangKyRequest {
  maDeTai: string;
  maSv: string;
}

export interface ThanhVienHoiDong {
  id: number;
  maHoiDong: string;
  maGv: string;
  tenGv: string;
  vaiTro: string;
}

export interface PhanCongDeTai {
  id: number;
  maHoiDong: string;
  maDeTai: string;
  tenDeTai: string;
}

export interface HoiDong {
  maHoiDong: string;
  tenHoiDong: string;
  loaiHoiDong: string;
  ngayHop: string;
  diaDiem: string;
  trangThai: string;
  thanhVienList: ThanhVienHoiDong[];
  deTaiList: PhanCongDeTai[];
}

export interface Diem {
  id: number;
  maDeTai: string;
  maGv: string;
  tenGv: string;
  loaiDiem: string;
  diemSo: number;
  nhanXet: string;
  ngayCham: string;
}

export interface DiemTrungBinh {
  maDeTai: string;
  diemTrungBinh: number;
  soLuongPhieuCham: number;
  xepLoai: string;
}

export interface ThongKeTrangThai {
  tongSoDeTai: number;
  choDuyet: number;
  daDuyet: number;
  dangThucHien: number;
  choNghiemThu: number;
  daNghiemThu: number;
  daHuy: number;
  phanBoTheoTrangThai: Record<string, number>;
}

export interface TiLeKetQua {
  kyBaoCao: string;
  tongSo: number;
  soLuongDat: number;
  soLuongKhongDat: number;
  tyLeDatPhanTram: number;
}

export interface TienDoDeTai {
  dungTienDo: number;
  chamTienDo: number;
  daHoanThanh: number;
}

export interface ThongBao {
  id: number;
  maNguoiNhan: string;
  tieuDe: string;
  noiDung: string;
  loaiThongBao: string;
  daDoc: boolean;
  ngayGui: string;
}

export interface TaiLieu {
  id: number;
  maDeTai: string;
  tenFile: string;
  duongDan: string;
  loaiTaiLieu: string;
  kichThuoc: number;
  nguoiUpload: string;
  ngayUpload: string;
}
