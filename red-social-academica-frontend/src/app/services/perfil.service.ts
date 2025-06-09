// src/app/services/perfil.service.ts
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

export interface InvitationDTO {
id: number;
senderId: number;
senderName: string;
receiverId: number;
receiverName: string;
fechaAlta: string;
fechaModificacion: string;
motivoBaja?: string;
fechaBaja?: string;
activo: boolean;
}

export interface InvitationCreateDTO {
receiverUsername: string;
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

  // ✅ Métodos relacionados con invitaciones
  getInvitacionesPendientes(username: string): Observable<InvitationDTO[]> {
    return this.http.get<InvitationDTO[]>(
      `/api/invitaciones/pendientes/recibidas/${username}`,
      { headers: this.getAuthHeaders() }
    );
  }

  enviarInvitacion(senderUsername: string, dto: InvitationCreateDTO): Observable<InvitationDTO> {
    return this.http.post<InvitationDTO>(
      `/api/invitaciones/${senderUsername}`,
      dto,
      { headers: this.getAuthHeaders() }
    );
  }

  aceptarInvitacion(invitationId: number, receiverUsername: string): Observable<InvitationDTO> {
    return this.http.put<InvitationDTO>(
      `/api/invitaciones/${invitationId}/aceptar`,
      null,
      {
        headers: this.getAuthHeaders().set('username', receiverUsername)
      }
    );
  }

  rechazarInvitacion(invitationId: number, username: string): Observable<InvitationDTO> {
    return this.http.put<InvitationDTO>(
      `/api/invitaciones/${invitationId}/rechazar`,
      null,
      {
        headers: this.getAuthHeaders().set('username', username)
      }
    );
  }
}
