import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserDTO {
id: number;
username: string;
email: string;
estado: string;
roles: string[];
}

@Injectable({
providedIn: 'root'
})
export class PerfilService {
private API_URL = '/api/usuarios';

constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  obtenerMiPerfil(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.API_URL}/me`, {
      headers: this.getAuthHeaders()
    });
  }
darDeBaja(): Observable<any> {
  return this.http.put<any>(`${this.API_URL}/me/baja`, null, {
    headers: this.getAuthHeaders()
  });
}

}
