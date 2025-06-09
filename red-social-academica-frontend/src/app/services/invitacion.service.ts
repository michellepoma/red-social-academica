import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
providedIn: 'root'
})
export class InvitacionService {
private baseUrl = '/api/invitaciones'; // <--- usa el proxy

constructor(private http: HttpClient) {}

 aceptarInvitacion(id: number): Observable<any> {
  return this.http.put(`${this.baseUrl}/${id}/aceptar`, {});
}


  rechazarInvitacion(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/rechazar`, {});
  }
}
