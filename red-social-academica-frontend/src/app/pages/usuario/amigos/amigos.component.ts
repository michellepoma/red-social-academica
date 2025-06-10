import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from 'src/app/services/user.service';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-amigos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './amigos.component.html'
})
export class AmigosComponent implements OnInit {
  amigos: any[] = [];
  paginaActual: number = 0;
  tamanioPagina: number = 10;
  totalPaginas: number = 0;
  cargando = true;
  error: string | null = null;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.cargarAmigos();
  }

  cargarAmigos(): void {
    this.cargando = true;
    this.userService.getAmigos(this.paginaActual, this.tamanioPagina).subscribe({
      next: (res) => {
        console.log('Respuesta de amigos:', res);
        this.amigos = res.content || [];
        this.totalPaginas = res.totalPages;
        this.error = this.amigos.length === 0 ? 'Aún no tienes amigos agregados.' : null;
        this.cargando = false;
      },
      error: () => {
        this.error = '❌ Error al cargar los amigos.';
        this.cargando = false;
      }
    });
  }

  siguientePagina(): void {
    if (this.paginaActual + 1 < this.totalPaginas) {
      this.paginaActual++;
      this.cargarAmigos();
    }
  }

  anteriorPagina(): void {
    if (this.paginaActual > 0) {
      this.paginaActual--;
      this.cargarAmigos();
    }
  }

  verPerfil(username: string): void {
    this.router.navigate(['/usuario/perfil', username]);
  }

  eliminar(username: string): void {
    if (confirm(`¿Estás seguro de eliminar a @${username} de tus amigos?`)) {
      this.userService.eliminarAmigo(username).subscribe({
        next: () => this.cargarAmigos(),
        error: () => this.error = '❌ No se pudo eliminar la amistad.'
      });
    }
  }
}
