import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, TaiLieu } from '../../../shared/models/api.model';

@Injectable({
  providedIn: 'root'
})
export class TaiLieuService {
  private readonly baseUrl = `${environment.apiUrl}/tai-lieu`;

  constructor(private http: HttpClient) {}

  getByDeTai(maDeTai: string): Observable<ApiResponse<TaiLieu[]>> {
    const params = new HttpParams().set('maDeTai', maDeTai);
    return this.http.get<ApiResponse<TaiLieu[]>>(this.baseUrl, { params });
  }

  upload(maDeTai: string, loaiTaiLieu: string, nguoiUpload: string, file: File): Observable<ApiResponse<TaiLieu>> {
    const formData = new FormData();
    formData.append('maDeTai', maDeTai);
    formData.append('loaiTaiLieu', loaiTaiLieu);
    formData.append('nguoiUpload', nguoiUpload);
    formData.append('file', file);
    return this.http.post<ApiResponse<TaiLieu>>(`${this.baseUrl}/upload`, formData);
  }

  getDownloadUrl(id: number): string {
    return `${this.baseUrl}/${id}/tai-ve`;
  }

  delete(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }
}
