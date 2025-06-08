import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UserService } from '../../../services/user.service'; // ✅ Ruta relativa corregida

@Component({
selector: 'app-admin-usuarios-por-rol',
standalone: true,
templateUrl: './admin-usuarios-por-rol.component.html',
styleUrls: ['./admin-usuarios-por-rol.component.scss'],
imports: [CommonModule, FormsModule, RouterModule]
})
export class AdminUsuariosPorRolComponent implements OnInit {
rol: string = '';
usuarios: any[] = [];
paginaActual: number = 0;
totalPaginas: number = 0;
tamanioPagina: number = 5;
mensaje: string = '';

constructor(private userService: UserService) {}

  ngOnInit(): void {}

  buscarPorRol(): void {
    if (!this.rol.trim()) {
      this.mensaje = 'Debes ingresar un rol.';
      this.usuarios = [];
      return;
    }

    this.userService.listarUsuariosPorRol(this.rol.trim(), this.paginaActual, this.tamanioPagina).subscribe({
      next: (res: any) => {
        this.usuarios = res.content || [];
        this.totalPaginas = res.totalPages || 0;
        this.mensaje = this.usuarios.length === 0 ? 'No se encontraron usuarios con ese rol.' : '';
      },
      error: (err: any) => {
        console.error('Error al buscar usuarios por rol:', err);
        this.mensaje = 'Ocurrió un error al buscar usuarios.';
      }
    });
  }

  paginaSiguiente(): void {
    if (this.paginaActual + 1 < this.totalPaginas) {
      this.paginaActual++;
      this.buscarPorRol();
    }
  }

  paginaAnterior(): void {
    if (this.paginaActual > 0) {
      this.paginaActual--;
      this.buscarPorRol();
    }
  }

  getEstadoTexto(usuario: any): string {
    return usuario.activo ? 'Activo' : 'Inactivo';
  }

  getEstadoColor(usuario: any): string {
    return usuario.activo ? 'green' : 'red';
  }
}

