import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ThongKeService } from '../thong-ke/services/thong-ke.service';
import { DeTaiService } from '../de-tai/services/de-tai.service';
import { DeTai, ThongKeTrangThai } from '../../shared/models/api.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent implements OnInit {
  searchQuery = '';
  thongKe: ThongKeTrangThai | null = null;
  recentDeTai: DeTai[] = [];

  constructor(
    private thongKeService: ThongKeService,
    private deTaiService: DeTaiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.thongKeService.getThongKeDeTai().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.thongKe = res.data;
        }
      },
      error: () => {}
    });

    this.deTaiService.getAll().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.recentDeTai = res.data.slice(0, 5);
        }
      },
      error: () => {}
    });
  }

  onSearch(): void {
    this.router.navigate(['/de-tai'], { queryParams: { q: this.searchQuery.trim() || undefined } });
  }
}
