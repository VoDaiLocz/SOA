import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from './services/user.service';
import { SinhVien } from '../../shared/models/api.model';

@Component({
  selector: 'app-sinh-vien-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sinh-vien-list.html'
})
export class SinhVienListComponent implements OnInit {
  list: SinhVien[] = [];
  searchText = '';
  showForm = false;
  errorMsg = '';
  successMsg = '';

  form: Partial<SinhVien> = {
    maSv: '', hoTen: '', email: '', soDienThoai: '', chuyenNganh: 'Công nghệ phần mềm', khoa: 'CNTT', lop: '', gpa: 3.2, trangThai: 'DANG_HOC'
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.userService.getAllSinhVien().subscribe({
      next: (res) => { if (res.success && res.data) this.list = res.data; },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi tải danh sách sinh viên.'
    });
  }

  get filtered(): SinhVien[] {
    if (!this.searchText.trim()) return this.list;
    const q = this.searchText.toLowerCase();
    return this.list.filter(sv =>
      sv.maSv?.toLowerCase().includes(q) ||
      sv.hoTen?.toLowerCase().includes(q) ||
      sv.lop?.toLowerCase().includes(q)
    );
  }

  onCreate(): void {
    this.errorMsg = '';
    this.successMsg = '';
    this.userService.createSinhVien(this.form).subscribe({
      next: () => {
        this.successMsg = 'Thêm sinh viên thành công.';
        this.showForm = false;
        this.load();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Thêm thất bại.'
    });
  }

  onDelete(maSv: string): void {
    if (!confirm(`Xóa sinh viên ${maSv}?`)) return;
    this.userService.deleteSinhVien(maSv).subscribe({
      next: () => {
        this.successMsg = `Đã xóa sinh viên ${maSv}.`;
        this.load();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Xóa thất bại.'
    });
  }
}
