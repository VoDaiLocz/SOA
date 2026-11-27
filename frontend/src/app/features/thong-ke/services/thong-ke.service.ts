import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, ThongKeTrangThai, TienDoDeTai, TiLeKetQua } from '../../../shared/models/api.model';

@Injectable({
  providedIn: 'root'
})
export class ThongKeService {
  private readonly baseUrl = `${environment.apiUrl}/thong-ke`;

  constructor(private http: HttpClient) {}

  getThongKeDeTai(): Observable<ApiResponse<ThongKeTrangThai>> {
    return this.http.get<ApiResponse<ThongKeTrangThai>>(`${this.baseUrl}/detai`);
  }

  getTiLeKetQua(khoa?: string): Observable<ApiResponse<TiLeKetQua>> {
    let params = new HttpParams();
    if (khoa) {
      params = params.set('khoa', khoa);
    }
    return this.http.get<ApiResponse<TiLeKetQua>>(`${this.baseUrl}/ket-qua`, { params });
  }

  getTienDo(): Observable<ApiResponse<TienDoDeTai>> {
    return this.http.get<ApiResponse<TienDoDeTai>>(`${this.baseUrl}/ti-do`);
  }

  getExportCsvUrl(): string {
    return `${this.baseUrl}/xuat?dinhDang=excel`;
  }
}
