import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  mode: 'login' | 'register' | 'change-password' = 'login';
  errorMsg = '';
  successMsg = '';
  loading = false;

  loginForm = { tenDangNhap: '', matKhau: '' };
  registerForm = { tenDangNhap: '', matKhau: '', hoTen: '', email: '', maSinhVien: '' };
  pwdForm = { tenDangNhap: '', matKhauCu: '', matKhauMoi: '' };

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(): void {
    this.errorMsg = '';
    this.loading = true;
    this.authService.login(this.loginForm).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.error?.message || 'Đăng nhập thất bại. Vui lòng kiểm tra lại.';
      }
    });
  }

  onRegister(): void {
    this.errorMsg = '';
    this.loading = true;
    this.authService.register(this.registerForm).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.error?.message || 'Đăng ký thất bại.';
      }
    });
  }

  onChangePassword(): void {
    this.errorMsg = '';
    this.successMsg = '';
    this.loading = true;
    this.authService.doiMatKhau(this.pwdForm.tenDangNhap, this.pwdForm.matKhauCu, this.pwdForm.matKhauMoi).subscribe({
      next: () => {
        this.loading = false;
        this.successMsg = 'Đổi mật khẩu thành công.';
        this.pwdForm = { tenDangNhap: '', matKhauCu: '', matKhauMoi: '' };
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.error?.message || 'Đổi mật khẩu thất bại.';
      }
    });
  }
}
