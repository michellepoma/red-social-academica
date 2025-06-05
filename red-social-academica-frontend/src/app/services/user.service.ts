import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private API_URL = '/api/admin/usuarios'; // Proxy lo redirige a localhost:8080

  constructor(private http: HttpClient) {}

 private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token'); // asegúrate que exista
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }
  getAllUsers(): Observable<any[]> {
    return this.http.get<any>(`${this.API_URL}/listar`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map(response => response.content || []) // <-- Corrige el acceso
    );
  }

darDeBaja(username: string): Observable<any> {
  return this.http.put(`/api/admin/usuarios/${username}/baja`, null);
}






}
