import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Comentario {
  id: number;
  content: string;
  createdAt: string; // puede ser Date, pero string es más flexible para formateo
  authorId: number;
  authorUsername: string;
  authorFullName: string;
  postId: number;
  fechaAlta: string;
  fechaModificacion: string;
  motivoBaja: string | null;
  fechaBaja: string | null;
  activo: boolean;
}

@Injectable({ providedIn: 'root' })
export class ComentarioService {
  private baseUrl = '/api/comments';

  constructor(private http: HttpClient) {}

  obtenerComentariosDeUsuario(username: string): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.baseUrl}/usuario/${username}`);
  }

  obtenerComentariosDeUsuarioPaginado(username: string, page = 0, size = 10): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/usuario/${username}/paginado?page=${page}&size=${size}`);
  }

  obtenerComentariosPorPost(postId: number): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(`${this.baseUrl}/post/${postId}`);
  }

  crearComentario(dto: { postId: number, content: string }): Observable<any> {
    return this.http.post(this.baseUrl, dto);
  }

  actualizarComentario(commentId: number, content: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${commentId}`, { content });
  }

eliminarComentario(commentId: number, motivo: string): Observable<any> {
  return this.http.put(`${this.baseUrl}/${commentId}/baja?motivo=${encodeURIComponent(motivo)}`, {});
}

//ADMIN
// ✔ PUT /api/admin/comments/{commentId}
actualizarComentarioComoAdmin(commentId: number, content: string): Observable<any> {
  return this.http.put(`/api/admin/comments/${commentId}`, { content });
}

// ✔ DELETE /api/admin/comments/{commentId}
eliminarComentarioComoAdmin(commentId: number): Observable<any> {
  return this.http.delete(`/api/admin/comments/${commentId}`);
}



}