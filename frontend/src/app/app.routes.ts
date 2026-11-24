import { Routes } from '@angular/router';

export const routes: Routes = [
  // Khung routing cho các tính năng tương ứng 9 microservices Backend
  // { path: 'auth', loadComponent: () => import('./features/auth/login.component').then(m => m.LoginComponent) },
  // { path: 'de-tai', loadComponent: () => import('./features/de-tai/de-tai.component').then(m => m.DeTaiComponent) },
  // { path: 'sinh-vien', loadComponent: () => import('./features/sinh-vien/sinh-vien.component').then(m => m.SinhVienComponent) },
  // { path: 'giang-vien', loadComponent: () => import('./features/giang-vien/giang-vien.component').then(m => m.GiangVienComponent) },
  // { path: 'hoi-dong', loadComponent: () => import('./features/hoi-dong/hoi-dong.component').then(m => m.HoiDongComponent) },
  // { path: 'cham-diem', loadComponent: () => import('./features/cham-diem/cham-diem.component').then(m => m.ChamDiemComponent) },
  // { path: 'thong-ke', loadComponent: () => import('./features/thong-ke/thong-ke.component').then(m => m.ThongKeComponent) },
  // { path: 'tai-lieu', loadComponent: () => import('./features/tai-lieu/tai-lieu.component').then(m => m.TaiLieuComponent) },
  { path: '', redirectTo: '', pathMatch: 'full' }
];
