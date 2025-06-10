import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Publicacion {
  id: number;
  title: string;
  text: string;
  date: string;
  imageUrl: string;
  resourceUrl: string;
  eventDate: string;
  authorFullName: string;
}

export interface PublicacionesPage {
  content: Publicacion[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = '/api/posts';

  constructor(private http: HttpClient) { }

  // ✔ GET /api/posts/{postId}
  getPostById(postId: number): Observable<Publicacion> {
    return this.http.get<Publicacion>(`${this.apiUrl}/${postId}`);
  }

  // ✔ PUT /api/posts/{postId}
  actualizarPublicacion(postId: number, data: Partial<Publicacion>): Observable<Publicacion> {
    return this.http.put<Publicacion>(`${this.apiUrl}/${postId}`, data);
  }

  // ✔ PUT /api/posts/{postId}/baja
  eliminarPublicacion(postId: number): Observable<any> {
    const motivo = encodeURIComponent('Eliminado por el usuario');
    return this.http.put(`${this.apiUrl}/${postId}/baja?motivo=${motivo}`, {});
  }


  // ✔ POST /api/posts
  crearPublicacion(data: any): Observable<Publicacion> {
    return this.http.post<Publicacion>(this.apiUrl, data);
  }

  // ✔ GET /api/posts/usuario/{username}
  obtenerPublicacionesDeUsuario(username: string, page = 0, size = 10): Observable<PublicacionesPage> {
    return this.http.get<PublicacionesPage>(`${this.apiUrl}/usuario/${username}?page=${page}&size=${size}`);
  }

  // ✔ GET /api/posts/recientes
  obtenerPublicacionesRecientes(): Observable<Publicacion[]> {
    return this.http.get<Publicacion[]>(`${this.apiUrl}/recientes`);
  }

  // ✔ GET /api/posts/buscar?texto=IA
  buscarPublicaciones(texto: string): Observable<Publicacion[]> {
    return this.http.get<Publicacion[]>(`${this.apiUrl}/buscar?texto=${encodeURIComponent(texto)}`);
  }

  //ADMIN
  // ✔ GET /api/admin/posts/{postId}
obtenerPostPorIdAdmin(postId: number): Observable<Publicacion> {
  return this.http.get<Publicacion>(`/api/admin/posts/${postId}`);
}

// ✔ PUT /api/admin/posts/{postId}
actualizarPostAdmin(postId: number, data: Partial<Publicacion>): Observable<Publicacion> {
  return this.http.put<Publicacion>(`/api/admin/posts/${postId}`, data);
}

// ✔ DELETE /api/admin/posts/{postId}
eliminarPostAdmin(postId: number): Observable<any> {
  return this.http.delete(`/api/admin/posts/${postId}`);
}

}
