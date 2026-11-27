import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DeTaiService } from './services/de-tai.service';
import { AuthService } from '../../core/services/auth.service';
import { DeTai, TaoDeTaiRequest } from '../../shared/models/api.model';

@Component({
  selector: 'app-de-tai-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './de-tai-list.html',
  styleUrl: './de-tai-list.css'
})
export class DeTaiListComponent implements OnInit {
  deTaiList: DeTai[] = [];
  filterStatus = '';
  searchText = '';
  showCreateForm = false;
  errorMsg = '';
  successMsg = '';

  newDeTai: TaoDeTaiRequest = {
    maDeTai: '',
    tenDeTai: '',
    moTa: '',
    linhVuc: 'Công nghệ thông tin',
    kinhPhiDuKien: 15000000,
    maSvChuNhiem: '',
    maGvhd: '',
    tenGvhd: '',
    soLuongToiDa: 3
  };

  constructor(
    private deTaiService: DeTaiService,
    public authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['q']) {
        this.searchText = params['q'];
      }
      this.loadDeTai();
    });
  }

  loadDeTai(): void {
    this.deTaiService.getAll(this.filterStatus || undefined).subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.deTaiList = res.data;
        }
      },
      error: (err) => {
        this.errorMsg = err?.error?.message || 'Không thể tải danh sách đề tài.';
      }
    });
  }

  get filteredList(): DeTai[] {
    if (!this.searchText.trim()) return this.deTaiList;
    const q = this.searchText.toLowerCase();
    return this.deTaiList.filter(dt =>
      dt.maDeTai?.toLowerCase().includes(q) ||
      dt.tenDeTai?.toLowerCase().includes(q) ||
      dt.linhVuc?.toLowerCase().includes(q) ||
      dt.tenGvhd?.toLowerCase().includes(q)
    );
  }

  onCreate(): void {
    this.errorMsg = '';
    this.successMsg = '';
    this.deTaiService.create(this.newDeTai).subscribe({
      next: () => {
        this.successMsg = 'Tạo đề tài mới thành công!';
        this.showCreateForm = false;
        this.loadDeTai();
      },
      error: (err) => {
        this.errorMsg = err?.error?.message || 'Tạo đề tài thất bại.';
      }
    });
  }

  onDuyet(maDeTai: string): void {
    this.deTaiService.duyet(maDeTai).subscribe({
      next: () => {
        this.successMsg = `Đã duyệt đề tài ${maDeTai}`;
        this.loadDeTai();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi duyệt đề tài.'
    });
  }

  onHuy(maDeTai: string): void {
    this.deTaiService.huy(maDeTai).subscribe({
      next: () => {
        this.successMsg = `Đã hủy đề tài ${maDeTai}`;
        this.loadDeTai();
      },
      error: (err) => this.errorMsg = err?.error?.message || 'Lỗi hủy đề tài.'
    });
  }

  onDangKy(maDeTai: string): void {
    const user = this.authService.getCurrentUser();
    const maSv = user?.maDinhDanh || prompt('Nhập Mã Sinh Viên đăng ký tham gia:');
    if (!maSv) return;

    this.errorMsg = '';
    this.successMsg = '';
    this.deTaiService.dangKy({ maDeTai, maSv }).subscribe({
      next: () => {
        this.successMsg = `Sinh viên ${maSv} đã đăng ký tham gia đề tài ${maDeTai}`;
        this.loadDeTai();
      },
      error: (err) => {
        this.errorMsg = err?.error?.message || 'Đăng ký đề tài thất bại.';
      }
    });
  }
}
