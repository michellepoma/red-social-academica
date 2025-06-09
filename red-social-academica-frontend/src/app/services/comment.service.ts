import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CommentCreateDTO {
postId: number;
content: string;
}

export interface CommentUpdateDTO {
content: string;
}

export interface CommentDTO {
id: number;
content: string;
createdAt: string;
authorId: number;
authorFullName: string;
postId: number;
fechaAlta: string;
fechaModificacion: string;
fechaBaja: string;
motivoBaja: string;
activo: boolean;
}

@Injectable({ providedIn: 'root' })
export class CommentService {
private baseUrl = '/api/comments'; // Usa proxy.conf.json

constructor(private http: HttpClient) {}

  crearComentario(data: CommentCreateDTO): Observable<CommentDTO> {
    return this.http.post<CommentDTO>(this.baseUrl, data);
  }

  actualizarComentario(id: number, data: CommentUpdateDTO): Observable<CommentDTO> {
    return this.http.put<CommentDTO>(`${this.baseUrl}/${id}`, data);
  }

  eliminarComentario(id: number, motivo: string): Observable<CommentDTO> {
    return this.http.put<CommentDTO>(`${this.baseUrl}/${id}/baja?motivo=${encodeURIComponent(motivo)}`, {});
  }

  obtenerComentariosPorPost(postId: number): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(`${this.baseUrl}/post/${postId}`);
  }

  obtenerComentariosPorUsuario(username: string): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(`${this.baseUrl}/usuario/${username}`);
  }
}
