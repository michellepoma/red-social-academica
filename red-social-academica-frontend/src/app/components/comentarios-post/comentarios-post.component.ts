import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CommentService, CommentDTO, CommentCreateDTO } from 'src/app/services/comment.service';

@Component({
selector: 'app-comentarios-post',
standalone: true,
imports: [CommonModule, FormsModule],
templateUrl: './comentarios-post.component.html',
styleUrls: ['./comentarios-post.component.scss']
})
export class ComentariosPostComponent implements OnInit {
@Input() postId!: number;
comentarios: CommentDTO[] = [];
nuevoComentario: string = '';
cargando = false;

constructor(private commentService: CommentService) {}

  ngOnInit(): void {
    this.cargarComentarios();
  }

  cargarComentarios(): void {
    if (!this.postId) return;
    this.cargando = true;

    this.commentService.obtenerComentariosPorPost(this.postId).subscribe({
      next: (res) => {
        this.comentarios = res;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar comentarios', err);
        this.cargando = false;
      }
    });
  }

  enviarComentario(): void {
    if (this.nuevoComentario.trim().length < 2) return;

    const dto: CommentCreateDTO = {
      postId: this.postId,
      content: this.nuevoComentario.trim()
    };

    this.commentService.crearComentario(dto).subscribe({
      next: (comentario) => {
        this.comentarios.push(comentario);
        this.nuevoComentario = '';
      },
      error: (err) => {
        console.error('Error al comentar', err);
        alert('Error al comentar');
      }
    });
  }

  eliminarComentario(id: number): void {
    const motivo = prompt('Motivo para eliminar este comentario:');
    if (!motivo) return;

    this.commentService.eliminarComentario(id, motivo).subscribe({
      next: () => {
        this.comentarios = this.comentarios.filter(c => c.id !== id);
      },
      error: (err) => {
        console.error('Error al eliminar comentario', err);
        alert('Error al eliminar');
      }
    });
  }
}

