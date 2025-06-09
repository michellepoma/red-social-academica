import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
providedIn: 'root'
})
export class UserService {
private API_URL = '/api/admin/usuarios';

constructor(private http: HttpClient) { }

  // --- Autenticación común ---
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // --- Gestión de usuarios (admin) ---
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
    return this.http.get(`/api/usuarios/${username}`, {
      headers: this.getAuthHeaders()
    });
  }

  editarUsuario(username: string, data: any): Observable<any> {
    return this.http.put(`${this.API_URL}/${username}`, data, {
      headers: this.getAuthHeaders()
    });
  }

  crearUsuario(usuario: any): Observable<any> {
    return this.http.post(`${this.API_URL}/crear`, usuario, {
      headers: this.getAuthHeaders()
    });
  }

  getTodosLosUsuarios(): Observable<any> {
    return this.http.get(`${this.API_URL}`, {
      headers: this.getAuthHeaders()
    });
  }

  buscarUsuarios(texto: string, page: number, size: number): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/buscar`, {
      params: {
        texto,
        page,
        size
      },
      headers: this.getAuthHeaders()
    });
  }

  // --- Perfil del usuario autenticado ---
  getPerfil(): Observable<any> {
    return this.http.get<any>(`/api/usuarios/me`, {
      headers: this.getAuthHeaders()
    });
  }

  getPerfilUsuario(username: string): Observable<any> {
    return this.http.get<any>(`/api/admin/usuarios/${username}`, {
      headers: this.getAuthHeaders()
    });
  }

  eliminarMiCuenta(): Observable<any> {
    return this.http.put(`/api/usuarios/me/baja`, null, {
      headers: this.getAuthHeaders()
    });
  }

  getAmigos(): Observable<any[]> {
    return this.http.get<any[]>('/api/usuarios/me/amigos', {
      headers: this.getAuthHeaders()
    });
  }

  getPerfilPublico(username: string): Observable<any> {
    return this.http.get(`/api/usuarios/${username}`, {
      headers: this.getAuthHeaders()
    });
  }

  // --- Invitaciones ---
  enviarInvitacion(senderUsername: string, receiverUsername: string): Observable<any> {
    const url = `/api/invitaciones/${senderUsername}`;
    const body = { receiverUsername };
    return this.http.post(url, body, {
      headers: this.getAuthHeaders()
    });
  }

  aceptarInvitacion(invitationId: number, username: string): Observable<any> {
    const headers = this.getAuthHeaders().set('username', username);
    return this.http.put(`/api/invitaciones/${invitationId}/aceptar`, null, { headers });
  }

  rechazarInvitacion(invitationId: number, username: string): Observable<any> {
    const headers = this.getAuthHeaders().set('username', username);
    return this.http.put(`/api/invitaciones/${invitationId}/rechazar`, null, { headers });
  }

  getInvitacionesRecibidas(username: string): Observable<any[]> {
    return this.http.get<any[]>(`/api/invitaciones/recibidas/${username}`, {
      headers: this.getAuthHeaders()
    });
  }

  getInvitacionesPendientes(username: string): Observable<any[]> {
    return this.http.get<any[]>(`/api/invitaciones/pendientes/recibidas/${username}`, {
      headers: this.getAuthHeaders()
    });
  }

  getInvitacionesEnviadas(username: string): Observable<any[]> {
    return this.http.get<any[]>(`/api/invitaciones/enviadas/${username}`, {
      headers: this.getAuthHeaders()
    });
  }

  cancelarInvitacion(invitationId: number, senderUsername: string): Observable<any> {
    const headers = this.getAuthHeaders().set('username', senderUsername);
    return this.http.put(`/api/invitaciones/${invitationId}/cancelar`, null, { headers });
  }
}
