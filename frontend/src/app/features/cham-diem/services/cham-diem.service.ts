import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, Diem, DiemTrungBinh } from '../../../shared/models/api.model';

@Injectable({
  providedIn: 'root'
})
export class ChamDiemService {
  private readonly baseUrl = `${environment.apiUrl}/diem`;

  constructor(private http: HttpClient) {}

  getByDeTai(maDeTai: string): Observable<ApiResponse<Diem[]>> {
    const params = new HttpParams().set('maDeTai', maDeTai);
    return this.http.get<ApiResponse<Diem[]>>(this.baseUrl, { params });
  }

  getTrungBinh(maDeTai: string): Observable<ApiResponse<DiemTrungBinh>> {
    return this.http.get<ApiResponse<DiemTrungBinh>>(`${this.baseUrl}/trung-binh/${maDeTai}`);
  }

  nhapDiem(req: { maDeTai: string; maGv: string; tenGv: string; loaiDiem: string; diemSo: number; nhanXet: string }): Observable<ApiResponse<Diem>> {
    return this.http.post<ApiResponse<Diem>>(this.baseUrl, req);
  }
}
