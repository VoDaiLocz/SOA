import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ChamDiemService } from './services/cham-diem.service';
import { DeTaiService } from '../de-tai/services/de-tai.service';
import { DeTai, Diem, DiemTrungBinh } from '../../shared/models/api.model';

@Component({
  selector: 'app-cham-diem',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cham-diem.html'
})
export class ChamDiemComponent implements OnInit {
  deTaiList: DeTai[] = [];
  selectedMaDeTai = '';
  diemList: Diem[] = [];
  trungBinh: DiemTrungBinh | null = null;
  errorMsg = '';
  successMsg = '';

  form = { maGv: '', tenGv: '', loaiDiem: 'BAO_VE', diemSo: 8.5, nhanXet: '' };

  constructor(private chamDiemService: ChamDiemService, private deTaiService: DeTaiService) {}

  ngOnInit(): void {
    this.deTaiService.getAll().subscribe({
      next: (res) => {
        if (res.success && res.data && res.data.length > 0) {
          this.deTaiList = res.data;
          this.selectedMaDeTai = res.data[0].maDeTai;
          this.loadDiem();
        }
      }
    });
  }

  loadDiem(): void {
    if (!this.selectedMaDeTai) return;
    this.chamDiemService.getByDeTai(this.selectedMaDeTai).subscribe({
      next: (res) => { if (res.success && res.data) this.diemList = res.data; },
      error: () => this.diemList = []
    });
    this.chamDiemService.getTrungBinh(this.selectedMaDeTai).subscribe({
      next: (res) => { if (res.success && res.data) this.trungBinh = res.data; },
      error: () => this.trungBinh = null
    });
  }

  onSubmit(): void {
    if (!this.selectedMaDeTai) return;
    this.errorMsg = '';
    this.successMsg = '';
    this.chamDiemService.nhapDiem({
      maDeTai: this.selectedMaDeTai,
      ...this.form
    }).subscribe({
      next: () => {
        this.successMsg = 'Lưu phiếu điểm thành công!';
        this.loadDiem();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Lưu điểm thất bại.'
    });
  }
}
