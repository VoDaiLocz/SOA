import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, GiangVien, Khoa, Lop, SinhVien } from '../../../shared/models/api.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAllSinhVien(): Observable<ApiResponse<SinhVien[]>> {
    return this.http.get<ApiResponse<SinhVien[]>>(`${this.apiUrl}/sinhvien`);
  }

  createSinhVien(sv: Partial<SinhVien>): Observable<ApiResponse<SinhVien>> {
    return this.http.post<ApiResponse<SinhVien>>(`${this.apiUrl}/sinhvien`, sv);
  }

  deleteSinhVien(maSv: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.apiUrl}/sinhvien/${maSv}`);
  }

  getAllGiangVien(): Observable<ApiResponse<GiangVien[]>> {
    return this.http.get<ApiResponse<GiangVien[]>>(`${this.apiUrl}/giangvien`);
  }

  createGiangVien(gv: Partial<GiangVien>): Observable<ApiResponse<GiangVien>> {
    return this.http.post<ApiResponse<GiangVien>>(`${this.apiUrl}/giangvien`, gv);
  }

  getAllKhoa(): Observable<ApiResponse<Khoa[]>> {
    return this.http.get<ApiResponse<Khoa[]>>(`${this.apiUrl}/khoa`);
  }

  getLopByKhoa(maKhoa?: string): Observable<ApiResponse<Lop[]>> {
    let params = new HttpParams();
    if (maKhoa) {
      params = params.set('maKhoa', maKhoa);
    }
    return this.http.get<ApiResponse<Lop[]>>(`${this.apiUrl}/lop`, { params });
  }
}
