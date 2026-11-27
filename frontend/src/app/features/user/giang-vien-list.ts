import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from './services/user.service';
import { GiangVien } from '../../shared/models/api.model';

@Component({
  selector: 'app-giang-vien-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './giang-vien-list.html'
})
export class GiangVienListComponent implements OnInit {
  list: GiangVien[] = [];
  searchText = '';
  showForm = false;
  errorMsg = '';
  successMsg = '';

  form: Partial<GiangVien> = {
    maGv: '', hoTen: '', email: '', soDienThoai: '', hocVi: 'TS.', khoa: 'CNTT', boMon: '', huongNghienCuu: ''
  };

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.userService.getAllGiangVien().subscribe({
      next: (res) => { if (res.success && res.data) this.list = res.data; },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi tải danh sách giảng viên.'
    });
  }

  get filtered(): GiangVien[] {
    if (!this.searchText.trim()) return this.list;
    const q = this.searchText.toLowerCase();
    return this.list.filter(g =>
      g.maGv?.toLowerCase().includes(q) ||
      g.hoTen?.toLowerCase().includes(q) ||
      g.khoa?.toLowerCase().includes(q) ||
      g.huongNghienCuu?.toLowerCase().includes(q)
    );
  }

  onCreate(): void {
    this.errorMsg = '';
    this.successMsg = '';
    this.userService.createGiangVien(this.form).subscribe({
      next: () => {
        this.successMsg = 'Thêm giảng viên thành công.';
        this.showForm = false;
        this.load();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Thêm thất bại.'
    });
  }
}
