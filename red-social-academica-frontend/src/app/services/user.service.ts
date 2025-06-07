import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
providedIn: 'root'
})
export class UserService {
private API_URL = '/api/admin/usuarios';

constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getUsuariosPaginado(page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/listar?page=${page}&size=${size}`, {
      headers: this.getAuthHeaders()
    });
  }

  darDeBaja(username: string): Observable<any> {
    return this.http.put(`${this.API_URL}/${username}/baja`, null, {
      headers: this.getAuthHeaders()
    });
  }

  getUsuarioPorUsername(username: string): Observable<any> {
    return this.http.get(`${this.API_URL}/${username}`, {
      headers: this.getAuthHeaders()
    });
  }

  editarUsuario(username: string, data: any): Observable<any> {
    return this.http.put(`${this.API_URL}/${username}`, data, {
      headers: this.getAuthHeaders()
    });
  }
  crearUsuario(usuario: any): Observable<any> {
    return this.http.post('/api/admin/usuarios/crear', usuario, {
      headers: this.getAuthHeaders()
    });
  }

}

