import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home';
import { LoginComponent } from './features/auth/login/login';
import { DeTaiListComponent } from './features/de-tai/de-tai-list';
import { GiangVienListComponent } from './features/user/giang-vien-list';
import { SinhVienListComponent } from './features/user/sinh-vien-list';
import { HoiDongListComponent } from './features/hoi-dong/hoi-dong-list';
import { ChamDiemComponent } from './features/cham-diem/cham-diem';
import { ThongKeComponent } from './features/thong-ke/thong-ke';
import { TaiLieuComponent } from './features/tai-lieu/tai-lieu';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'auth/login', component: LoginComponent },
  { path: 'login', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: 'de-tai', component: DeTaiListComponent },
  { path: 'giang-vien', component: GiangVienListComponent },
  { path: 'sinh-vien', component: SinhVienListComponent },
  { path: 'hoi-dong', component: HoiDongListComponent },
  { path: 'cham-diem', component: ChamDiemComponent },
  { path: 'thong-ke', component: ThongKeComponent },
  { path: 'tai-lieu', component: TaiLieuComponent },
  { path: '**', redirectTo: '' }
];
