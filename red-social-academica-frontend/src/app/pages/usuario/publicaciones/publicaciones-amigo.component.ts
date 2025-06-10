import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from 'src/app/services/post.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-publicaciones-amigo',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './publicaciones-amigo.component.html'
})
export class PublicacionesAmigoComponent implements OnInit {
  publicaciones: any[] = [];
  username: string = '';
  mensaje: string = '';
  paginaActual: number = 0;
  tamanioPagina: number = 6;
  totalPaginas: number = 0;

  constructor(private route: ActivatedRoute, private postService: PostService) {}

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username')!;
    this.cargarPublicaciones();
  }

  cargarPublicaciones(): void {
    this.postService.obtenerPublicacionesDeOtroUsuario(this.username, this.paginaActual, this.tamanioPagina)
      .subscribe({
        next: (res: any) => {
          this.publicaciones = res.content || [];
          this.totalPaginas = res.totalPages;
          this.mensaje = this.publicaciones.length === 0 ? 'Este usuario aún no tiene publicaciones.' : '';
        },
        error: () => {
          this.mensaje = '❌ Error al cargar las publicaciones.';
        }
      });
  }

  siguientePagina(): void {
    if (this.paginaActual + 1 < this.totalPaginas) {
      this.paginaActual++;
      this.cargarPublicaciones();
    }
  }

  anteriorPagina(): void {
    if (this.paginaActual > 0) {
      this.paginaActual--;
      this.cargarPublicaciones();
    }
  }
}
