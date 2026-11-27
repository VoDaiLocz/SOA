import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse, ThongBao } from '../../shared/models/api.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationApiService {
  private readonly baseUrl = `${environment.apiUrl}/thong-bao`;

  constructor(private http: HttpClient) {}

  getByNguoiNhan(maNguoiNhan: string): Observable<ApiResponse<ThongBao[]>> {
    return this.http.get<ApiResponse<ThongBao[]>>(`${this.baseUrl}/${maNguoiNhan}`);
  }

  danhDauDaDoc(id: number): Observable<ApiResponse<void>> {
    return this.http.put<ApiResponse<void>>(`${this.baseUrl}/${id}/da-doc`, {});
  }

  guiThongBao(maNguoiNhan: string, tieuDe: string, noiDung: string, loaiThongBao: string): Observable<ApiResponse<ThongBao>> {
    return this.http.post<ApiResponse<ThongBao>>(`${this.baseUrl}/gui`, {
      maNguoiNhan,
      tieuDe,
      noiDung,
      loaiThongBao
    });
  }
}
