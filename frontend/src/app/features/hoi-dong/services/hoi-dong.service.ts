import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, HoiDong } from '../../../shared/models/api.model';

@Injectable({
  providedIn: 'root'
})
export class HoiDongService {
  private readonly baseUrl = `${environment.apiUrl}/hoi-dong`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<ApiResponse<HoiDong[]>> {
    return this.http.get<ApiResponse<HoiDong[]>>(this.baseUrl);
  }

  getByMa(maHoiDong: string): Observable<ApiResponse<HoiDong>> {
    return this.http.get<ApiResponse<HoiDong>>(`${this.baseUrl}/${maHoiDong}`);
  }

  create(req: { maHoiDong: string; tenHoiDong: string; loaiHoiDong: string; ngayHop: string; diaDiem: string }): Observable<ApiResponse<HoiDong>> {
    return this.http.post<ApiResponse<HoiDong>>(this.baseUrl, req);
  }

  phanCongGiangVien(maHoiDong: string, req: { maGv: string; tenGv: string; vaiTro: string }): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/${maHoiDong}/phan-cong`, req);
  }

  ganDeTai(maHoiDong: string, req: { maDeTai: string; tenDeTai: string }): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/${maHoiDong}/gan-de-tai`, req);
  }
}
