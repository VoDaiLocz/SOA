import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThongKeService } from './services/thong-ke.service';
import { ThongKeTrangThai, TienDoDeTai, TiLeKetQua } from '../../shared/models/api.model';

@Component({
  selector: 'app-thong-ke',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './thong-ke.html'
})
export class ThongKeComponent implements OnInit {
  trangThai: ThongKeTrangThai | null = null;
  ketQua: TiLeKetQua | null = null;
  tienDo: TienDoDeTai | null = null;
  errorMsg = '';

  constructor(public thongKeService: ThongKeService) {}

  ngOnInit(): void {
    this.thongKeService.getThongKeDeTai().subscribe({
      next: (res) => { if (res.success) this.trangThai = res.data; },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi tải thống kê.'
    });
    this.thongKeService.getTiLeKetQua().subscribe({
      next: (res) => { if (res.success) this.ketQua = res.data; }
    });
    this.thongKeService.getTienDo().subscribe({
      next: (res) => { if (res.success) this.tienDo = res.data; }
    });
  }

  get statusEntries(): { key: string; val: number }[] {
    if (!this.trangThai?.phanBoTheoTrangThai) return [];
    return Object.entries(this.trangThai.phanBoTheoTrangThai).map(([key, val]) => ({ key, val }));
  }
}
