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
  mode: 'login' | 'register' | 'change-password' | 'forgot-password' = 'login';
  errorMsg = '';
  successMsg = '';
  loading = false;

  // Forgot password step 1 (request OTP) vs step 2 (verify OTP & new pass)
  forgotStep: 1 | 2 = 1;

  loginForm = { tenDangNhap: '', matKhau: '' };
  registerForm = { tenDangNhap: '', matKhau: '', hoTen: '', email: '', maSinhVien: '' };
  pwdForm = { tenDangNhap: '', matKhauCu: '', matKhauMoi: '' };
  forgotForm = { emailOrUser: '', otp: '', matKhauMoi: '' };

  constructor(private authService: AuthService, private router: Router) {}

  switchMode(newMode: 'login' | 'register' | 'change-password' | 'forgot-password'): void {
    this.mode = newMode;
    this.errorMsg = '';
    this.successMsg = '';
    this.forgotStep = 1;
  }

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

  onRequestOtp(): void {
    if (!this.forgotForm.emailOrUser.trim()) {
      this.errorMsg = 'Vui lòng nhập tên đăng nhập hoặc email đăng ký.';
      return;
    }
    this.errorMsg = '';
    this.successMsg = '';
    this.loading = true;
    this.authService.quenMatKhau(this.forgotForm.emailOrUser).subscribe({
      next: (res) => {
        this.loading = false;
        this.forgotStep = 2;
        const msg = res?.data?.thongBao || res?.message || 'Mã xác thực OTP đã được gửi về email của bạn!';
        this.successMsg = msg;

        // Nếu thông báo chứa mã OTP test trong ngoặc vuông [123456], tự động điền sẵn
        const match = msg.match(/\[(\d{6})\]/);
        if (match && match[1]) {
          this.forgotForm.otp = match[1];
        }
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.error?.message || 'Không tìm thấy tài khoản hoặc lỗi gửi email OTP.';
      }
    });
  }

  onResetPassword(): void {
    if (!this.forgotForm.otp.trim() || !this.forgotForm.matKhauMoi.trim()) {
      this.errorMsg = 'Vui lòng nhập đầy đủ mã OTP và mật khẩu mới.';
      return;
    }
    this.errorMsg = '';
    this.successMsg = '';
    this.loading = true;
    this.authService.datLaiMatKhau(this.forgotForm.emailOrUser, this.forgotForm.otp, this.forgotForm.matKhauMoi).subscribe({
      next: () => {
        this.loading = false;
        this.successMsg = 'Đặt lại mật khẩu thành công! Bạn có thể chuyển sang tab Đăng nhập.';
        this.forgotStep = 1;
        this.forgotForm = { emailOrUser: '', otp: '', matKhauMoi: '' };
      },
      error: (err) => {
        this.loading = false;
        this.errorMsg = err?.error?.message || 'Mã OTP không hợp lệ hoặc đã hết hạn.';
      }
    });
  }
}
