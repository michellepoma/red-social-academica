import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InvitationService {
  private baseUrl = '/api/invitaciones'; // Usa proxy si tienes

  constructor(private http: HttpClient) {}

  obtenerTodasRecibidas(username: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/recibidas/${username}`);
  }

  aceptarInvitacion(id: number, username: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/aceptar`, {}, {
      headers: new HttpHeaders({ username })
    });
  }

  rechazarInvitacion(id: number, username: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/rechazar`, {}, {
      headers: new HttpHeaders({ username })
    });
  }

  enviarInvitacion(senderUsername: string, dto: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/${senderUsername}`, dto);
  }

  obtenerPendientesRecibidas(username: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/pendientes/recibidas/${username}`);
  }

  obtenerInvitacionesEnviadas(username: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/enviadas/${username}`);
  }

  cancelarInvitacion(id: number, senderUsername: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/cancelar`, {}, {
      headers: new HttpHeaders({ username: senderUsername })
    });
  }

  obtenerInvitacionesActivas(role: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/activas`, {
      headers: new HttpHeaders({ role })
    });
  }
}
