import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { AuthService } from 'src/app/services/auth.service';
import { PostService, Publicacion } from 'src/app/services/post.service';

@Component({
  selector: 'app-mis-publicaciones',
  standalone: true,
  templateUrl: './mis-publicaciones.component.html',
  styleUrls: ['./mis-publicaciones.component.scss'],
  imports: [CommonModule, RouterModule, FormsModule]
})
export class MisPublicacionesComponent implements OnInit {
  publicaciones: Publicacion[] = [];
  paginaActual: number = 0;
  tamanioPagina: number = 6;
  totalPaginas: number = 0;
  mensaje: string = '';
  username: string = ''; // Ahora garantizamos que no sea null
  textoBusqueda: string = '';
  modoRecientes: boolean = false;

  constructor(
    private postService: PostService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const nombreUsuario = this.authService.getUsername();
    if (nombreUsuario) {
      this.username = nombreUsuario;
      this.cargarPublicaciones();
    } else {
      this.mensaje = 'No se pudo obtener el nombre de usuario.';
    }
  }

  cargarPublicaciones(): void {
    this.postService.obtenerPublicacionesDeUsuario(this.username, this.paginaActual, this.tamanioPagina).subscribe({
      next: (res) => {
        this.publicaciones = res.content;
        this.totalPaginas = res.totalPages;
        this.mensaje = this.publicaciones.length === 0 ? 'No tienes publicaciones aún.' : '';
      },
      error: (err) => {
        console.error('Error al cargar publicaciones:', err);
        this.mensaje = 'Hubo un error al cargar las publicaciones.';
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
  darDeBaja(postId: number): void {
    if (!confirm('¿Estás seguro de eliminar esta publicación?')) return;

    this.postService.eliminarPublicacion(postId).subscribe({
      next: () => {
        alert('✅ Publicación eliminada correctamente.');
        this.cargarPublicaciones(); // Vuelve a cargar la lista
      },
      error: (err) => {
        console.error('Error al eliminar publicación:', err);
        alert('❌ Error al eliminar publicación.');
      }
    });
  }


  buscar(): void {
    const texto = this.textoBusqueda.trim();
    if (texto === '') {
      this.paginaActual = 0;
      this.cargarPublicaciones(); // método original de carga
      return;
    }

    this.postService.buscarPublicaciones(this.textoBusqueda).subscribe({
      next: (res: any) => {
        this.publicaciones = res.content ?? []; // 👈 nos aseguramos que sea array
        this.totalPaginas = res.totalPages;
      },
      error: (err) => {
        console.error('Error al buscar publicaciones:', err);
        this.publicaciones = []; // evitar errores en el HTML
      }
    });

  }

  limpiarBusqueda(): void {
    this.textoBusqueda = '';
    this.paginaActual = 0;
    this.cargarPublicaciones();
  }
  
  verRecientes(): void {
  this.postService.obtenerPublicacionesRecientes().subscribe({
    next: (res) => {
      this.publicaciones = res;
      this.totalPaginas = 1;
      this.paginaActual = 0;
      this.modoRecientes = true;
    },
    error: (err) => {
      console.error('Error al obtener publicaciones recientes:', err);
      this.mensaje = '❌ Error al obtener publicaciones recientes.';
    }
  });
}

verPropias(): void {
  this.paginaActual = 0;
  this.modoRecientes = false;
  this.cargarPublicaciones(); // tu método paginado
}

}
