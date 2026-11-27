import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { ApiResponse, DangKyRequest, DeTai, TaoDeTaiRequest } from '../../../shared/models/api.model';

@Injectable({
  providedIn: 'root'
})
export class DeTaiService {
  private readonly baseUrl = `${environment.apiUrl}/detai`;

  constructor(private http: HttpClient) {}

  getAll(trangThai?: string): Observable<ApiResponse<DeTai[]>> {
    let params = new HttpParams();
    if (trangThai) {
      params = params.set('trangThai', trangThai);
    }
    return this.http.get<ApiResponse<DeTai[]>>(this.baseUrl, { params });
  }

  getByMa(maDeTai: string): Observable<ApiResponse<DeTai>> {
    return this.http.get<ApiResponse<DeTai>>(`${this.baseUrl}/${maDeTai}`);
  }

  create(req: TaoDeTaiRequest): Observable<ApiResponse<DeTai>> {
    return this.http.post<ApiResponse<DeTai>>(this.baseUrl, req);
  }

  update(maDeTai: string, req: Partial<TaoDeTaiRequest>): Observable<ApiResponse<DeTai>> {
    return this.http.put<ApiResponse<DeTai>>(`${this.baseUrl}/${maDeTai}`, req);
  }

  delete(maDeTai: string): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${maDeTai}`);
  }

  duyet(maDeTai: string): Observable<ApiResponse<DeTai>> {
    return this.http.post<ApiResponse<DeTai>>(`${this.baseUrl}/${maDeTai}/duyet`, {});
  }

  huy(maDeTai: string): Observable<ApiResponse<DeTai>> {
    return this.http.post<ApiResponse<DeTai>>(`${this.baseUrl}/${maDeTai}/huy`, {});
  }

  giaHan(maDeTai: string): Observable<ApiResponse<DeTai>> {
    return this.http.post<ApiResponse<DeTai>>(`${this.baseUrl}/${maDeTai}/gia-han`, {});
  }

  dangKy(req: DangKyRequest): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/dang-ky`, req);
  }
}
