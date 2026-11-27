import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HoiDongService } from './services/hoi-dong.service';
import { HoiDong } from '../../shared/models/api.model';

@Component({
  selector: 'app-hoi-dong-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hoi-dong-list.html'
})
export class HoiDongListComponent implements OnInit {
  list: HoiDong[] = [];
  showCreate = false;
  errorMsg = '';
  successMsg = '';

  createForm = { maHoiDong: '', tenHoiDong: '', loaiHoiDong: 'BAO_VE_CHINH_THUC', ngayHop: '', diaDiem: '' };
  gvForm = { maHoiDong: '', maGv: '', tenGv: '', vaiTro: 'CHU_TICH' };
  dtForm = { maHoiDong: '', maDeTai: '', tenDeTai: '' };

  constructor(private hoiDongService: HoiDongService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.hoiDongService.getAll().subscribe({
      next: (res) => { if (res.success && res.data) this.list = res.data; },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi tải danh sách hội đồng.'
    });
  }

  onCreate(): void {
    this.errorMsg = '';
    this.successMsg = '';
    this.hoiDongService.create(this.createForm).subscribe({
      next: () => {
        this.successMsg = 'Thành lập hội đồng thành công.';
        this.showCreate = false;
        this.load();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi tạo hội đồng.'
    });
  }

  onAssignGv(): void {
    this.errorMsg = '';
    this.successMsg = '';
    this.hoiDongService.phanCongGiangVien(this.gvForm.maHoiDong, {
      maGv: this.gvForm.maGv,
      tenGv: this.gvForm.tenGv,
      vaiTro: this.gvForm.vaiTro
    }).subscribe({
      next: () => {
        this.successMsg = 'Phân công giảng viên thành công.';
        this.load();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi phân công giảng viên.'
    });
  }

  onAssignDeTai(): void {
    this.errorMsg = '';
    this.successMsg = '';
    this.hoiDongService.ganDeTai(this.dtForm.maHoiDong, {
      maDeTai: this.dtForm.maDeTai,
      tenDeTai: this.dtForm.tenDeTai
    }).subscribe({
      next: () => {
        this.successMsg = 'Gán đề tài vào hội đồng thành công.';
        this.load();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi gán đề tài.'
    });
  }
}
