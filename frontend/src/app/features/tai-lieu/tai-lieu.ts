import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TaiLieuService } from './services/tai-lieu.service';
import { DeTaiService } from '../de-tai/services/de-tai.service';
import { AuthService } from '../../core/services/auth.service';
import { DeTai, TaiLieu } from '../../shared/models/api.model';

@Component({
  selector: 'app-tai-lieu',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tai-lieu.html'
})
export class TaiLieuComponent implements OnInit {
  deTaiList: DeTai[] = [];
  selectedMaDeTai = '';
  loaiTaiLieu = 'THUYET_MINH';
  selectedFile: File | null = null;
  list: TaiLieu[] = [];
  errorMsg = '';
  successMsg = '';

  constructor(
    public taiLieuService: TaiLieuService,
    private deTaiService: DeTaiService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.deTaiService.getAll().subscribe({
      next: (res) => {
        if (res.success && res.data && res.data.length > 0) {
          this.deTaiList = res.data;
          this.selectedMaDeTai = res.data[0].maDeTai;
          this.loadFiles();
        }
      }
    });
  }

  loadFiles(): void {
    if (!this.selectedMaDeTai) return;
    this.taiLieuService.getByDeTai(this.selectedMaDeTai).subscribe({
      next: (res) => { if (res.success && res.data) this.list = res.data; },
      error: () => this.list = []
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  onUpload(): void {
    if (!this.selectedFile || !this.selectedMaDeTai) {
      this.errorMsg = 'Vui lòng chọn đề tài và file cần tải lên.';
      return;
    }
    const nguoiUpload = this.authService.getCurrentUser()?.tenDangNhap || 'sinhvien';
    this.errorMsg = '';
    this.successMsg = '';
    this.taiLieuService.upload(this.selectedMaDeTai, this.loaiTaiLieu, nguoiUpload, this.selectedFile).subscribe({
      next: () => {
        this.successMsg = 'Upload tài liệu thành công.';
        this.selectedFile = null;
        this.loadFiles();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Upload thất bại.'
    });
  }

  onDelete(id: number): void {
    if (!confirm('Xóa tài liệu này?')) return;
    this.taiLieuService.delete(id).subscribe({
      next: () => {
        this.successMsg = 'Đã xóa tài liệu.';
        this.loadFiles();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Xóa thất bại.'
    });
  }
}
