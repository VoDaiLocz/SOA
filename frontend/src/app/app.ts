import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from './core/services/auth.service';
import { NotificationApiService } from './core/services/notification-api.service';
import { ThongBao } from './shared/models/api.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent implements OnInit {
  title = 'CƠ SỞ DỮ LIỆU KHOA HỌC VÀ CÔNG NGHỆ';
  notifications: ThongBao[] = [];
  showNotifDropdown = false;
  mobileMenuOpen = false;

  constructor(
    public authService: AuthService,
    private notificationService: NotificationApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    const user = this.authService.getCurrentUser();
    if (user && user.maDinhDanh) {
      this.notificationService.getByNguoiNhan(user.maDinhDanh).subscribe({
        next: (res) => {
          if (res.success && res.data) {
            this.notifications = res.data;
          }
        },
        error: () => {}
      });
    }
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.daDoc).length;
  }

  toggleNotif(): void {
    this.showNotifDropdown = !this.showNotifDropdown;
    if (this.showNotifDropdown) {
      this.loadNotifications();
    }
  }

  markAsRead(tb: ThongBao): void {
    if (!tb.daDoc) {
      this.notificationService.danhDauDaDoc(tb.id).subscribe({
        next: () => {
          tb.daDoc = true;
        }
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.notifications = [];
    this.showNotifDropdown = false;
    this.router.navigate(['/auth/login']);
  }
}
