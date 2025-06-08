// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode'; // ✅ CORRECTA

interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private API_URL = 'http://localhost:8080/api/auth';

  constructor(private _http: HttpClient) {}

  login(payload: { username: string; password: string }): Observable<LoginResponse> {
    return this._http.post<LoginResponse>(`${this.API_URL}/login`, payload);
  }

  signup(payload: any): Observable<any> {
    return this._http.post<any>(`${this.API_URL}/signup`, payload);
  }

  saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

 getRoles(): string[] {
  const token = this.getToken();
  if (!token) return [];
  try {
    const decoded: any = jwtDecode(token);
    return decoded?.roles || [];
  } catch (e) {
    return [];
  }
}


  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }
}

