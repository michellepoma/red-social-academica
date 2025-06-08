import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service'; // ✅ Corregido

@Component({
selector: 'app-admin-usuarios-buscar',
standalone: true,
templateUrl: './admin-usuarios-buscar.component.html',
styleUrls: ['./admin-usuarios-buscar.component.scss'],
imports: [CommonModule, RouterModule, FormsModule]
})
export class AdminUsuariosBuscarComponent implements OnInit {
textoBusqueda: string = '';
usuarios: any[] = [];
paginaActual: number = 0;
totalPaginas: number = 0;
mensaje: string = '';

constructor(private userService: UserService) {}

  ngOnInit(): void {}

  buscar(): void {
    if (!this.textoBusqueda.trim()) {
      this.mensaje = 'Ingrese un texto para buscar.';
      this.usuarios = [];
      return;
    }

    this.userService.buscarUsuarios(this.textoBusqueda, this.paginaActual).subscribe({
      next: (res: any) => {
        this.usuarios = res.content;
        this.totalPaginas = res.totalPages;
        this.mensaje = this.usuarios.length === 0 ? 'No se encontraron usuarios.' : '';
      },
      error: (err: any) => {
        console.error('Error al buscar usuarios:', err);
        this.mensaje = 'Hubo un error al realizar la búsqueda.';
      }
    });
  }

  paginaSiguiente(): void {
    if (this.paginaActual + 1 < this.totalPaginas) {
      this.paginaActual++;
      this.buscar();
    }
  }

  paginaAnterior(): void {
    if (this.paginaActual > 0) {
      this.paginaActual--;
      this.buscar();
    }
  }
}
