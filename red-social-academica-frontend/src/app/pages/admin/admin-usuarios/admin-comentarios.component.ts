import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';


import { ComentarioService, Comentario } from 'src/app/services/comment.service';

@Component({
    selector: 'app-admin-comentarios',
    standalone: true,
    imports: [CommonModule, FormsModule],
    templateUrl: './admin-comentarios.component.html'
})
export class AdminComentariosComponent implements OnInit {
    comentarios: Comentario[] = [];
    comentarioEditandoId: number | null = null;
    contenidoEditado: string = '';
    mensaje: string = '';
    postId!: number;
    username: string = '';

    constructor(
        private route: ActivatedRoute,
        private comentarioService: ComentarioService,
        private http: HttpClient
    ) { }

    ngOnInit(): void {
        const postIdParam = this.route.snapshot.paramMap.get('postId');
        const usernameParam = this.route.snapshot.paramMap.get('username');

        if (postIdParam && usernameParam) {
            this.postId = Number(postIdParam);
            this.username = usernameParam;
            this.cargarComentarios();
        } else {
            this.mensaje = '❌ No se pudo identificar el post o usuario.';
        }
    }

    cargarComentarios(): void {
        this.comentarioService.obtenerComentariosPorPost(this.postId).subscribe({
            next: (res) => {
                this.comentarios = res;
                this.mensaje = this.comentarios.length === 0
                    ? 'Este post no tiene comentarios.'
                    : '';
            },
            error: (err) => {
                console.error('Error al cargar comentarios:', err);
                this.mensaje = '❌ Ocurrió un error al obtener los comentarios.';
            }
        });
    }

    editarComentario(comentario: Comentario): void {
        this.comentarioEditandoId = comentario.id;
        this.contenidoEditado = comentario.content;
    }

    cancelarEdicion(): void {
        this.comentarioEditandoId = null;
        this.contenidoEditado = '';
    }

    guardarEdicion(comentarioId: number): void {
        const contenido = this.contenidoEditado.trim();
        if (!contenido) return;

        this.comentarioService.actualizarComentarioComoAdmin(comentarioId, contenido).subscribe({
            next: () => {
                this.mensaje = '✅ Comentario actualizado.';
                this.comentarioEditandoId = null;
                this.contenidoEditado = '';
                this.cargarComentarios();
            },
            error: (err) => {
                console.error('Error al actualizar comentario:', err);
                this.mensaje = '❌ Error al guardar cambios.';
            }
        });
    }

    eliminarComentario(comentarioId: number): void {
        const confirmacion = confirm('¿Estás seguro de eliminar este comentario?');

        if (!confirmacion) return;

        const motivo = 'eliminado_por_admin';

        this.http.delete(`/api/admin/comments/${comentarioId}?motivo=${encodeURIComponent(motivo)}`).subscribe({
            next: () => {
                this.mensaje = '✅ Comentario eliminado.';
                this.cargarComentarios();
            },
            error: (err) => {
                console.error('Error al eliminar comentario:', err);
                this.mensaje = '❌ No se pudo eliminar el comentario.';
            }
        });
    }



}
