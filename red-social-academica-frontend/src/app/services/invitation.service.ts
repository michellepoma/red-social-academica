import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InvitationService {

  private baseUrl = 'http://localhost:8080/api/invitaciones';

  constructor(private http: HttpClient) {}

  // Enviar una invitación
  enviarInvitacion(destinatarioUsername: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/${destinatarioUsername}`, {});
  }

  // Aceptar invitación
  aceptarInvitacion(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/aceptar`, {});
  }

  // Rechazar invitación
  rechazarInvitacion(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/rechazar`, {});
  }

  // Cancelar invitación (opcional)
  cancelarInvitacion(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/cancelar`, {});
  }

  // Obtener invitaciones recibidas pendientes
  obtenerPendientesRecibidas(username: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/pendientes/recibidas/${username}`);
  }

  // Obtener invitaciones enviadas
  obtenerEnviadas(username: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/enviadas/${username}`);
  }

  // Obtener todas las activas (por si sos admin)
  obtenerActivas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/activas`);
  }
}
