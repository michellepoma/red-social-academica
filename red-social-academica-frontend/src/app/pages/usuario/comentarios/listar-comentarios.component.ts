import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ComentarioService, Comentario } from 'src/app/services/comment.service';
import { AuthService } from 'src/app/services/auth.service';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-mis-comentarios',
  standalone: true,
  templateUrl: './listar-comentarios.component.html',
  styleUrls: ['./listar-comentarios.component.scss'],
  imports: [CommonModule, FormsModule],
  providers: [DatePipe]
})
export class MisComentariosComponent implements OnInit {
  comentarios: Comentario[] = [];
  mensaje = '';
  postId!: number;
  username: string = '';
  nuevoComentario: string = '';

  comentarioEditandoId: number | null = null;
  comentarioEditado: string = '';

paginaActual: number = 0;
tamanioPagina: number = 5;
totalPaginas: number = 0;


  constructor(
    private route: ActivatedRoute,
    private comentarioService: ComentarioService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const postIdParam = this.route.snapshot.paramMap.get('postId');
    this.postId = postIdParam ? +postIdParam : 0;

    if (!this.postId || isNaN(this.postId)) {
      this.mensaje = 'ID de publicación inválido.';
      return;
    }

    this.username = this.authService.getUsername() ?? ''; // Para evitar error de null
    this.cargarComentarios();
  }

  cargarComentarios(): void {
    this.comentarioService.obtenerComentariosPorPost(this.postId).subscribe({
      next: (res) => {
        this.comentarios = res;
        if (res.length === 0) {
          this.mensaje = 'No hay comentarios aún para este post.';
        }
      },
      error: () => {
        this.mensaje = 'Error al cargar los comentarios.';
      }
    });
  }

  crearComentario(): void {
    const texto = this.nuevoComentario.trim();
    if (!texto) return;

    const dto = {
      postId: this.postId,
      content: texto
    };

    this.comentarioService.crearComentario(dto).subscribe({
      next: () => {
        this.nuevoComentario = '';
        this.cargarComentarios();
      },
      error: () => alert('❌ No se pudo publicar el comentario')
    });
  }

  editarComentario(com: Comentario): void {
    this.comentarioEditandoId = com.id;
    this.comentarioEditado = com.content;
  }

  cancelarEdicion(): void {
    this.comentarioEditandoId = null;
    this.comentarioEditado = '';
  }

  guardarEdicion(id: number): void {
    const texto = this.comentarioEditado.trim();
    if (!texto) return;

    this.comentarioService.actualizarComentario(id, texto).subscribe({
      next: () => {
        this.comentarioEditandoId = null;
        this.comentarioEditado = '';
        this.cargarComentarios();
      },
      error: () => alert('❌ Error al actualizar el comentario')
    });
  }

  eliminar(id: number): void {
    if (!confirm('¿Eliminar este comentario?')) return;

    this.comentarioService.eliminarComentario(id, 'Eliminado por el autor').subscribe({
      next: () => this.cargarComentarios(),
      error: () => alert('❌ No se pudo eliminar')
    });
  }
}
