// src/app/pages/admin/admin-usuarios/admin-usuarios-listar.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; // ✅ IMPORTANTE
import { UserService } from '../../../services/user.service';

@Component({
selector: 'app-admin-usuarios-listar',
standalone: true,
templateUrl: './admin-usuarios-listar.component.html',
styleUrls: ['./admin-usuarios-listar.component.scss'],
imports: [
CommonModule,
RouterModule // ✅ NECESARIO PARA USAR [routerLink]
]
})
export class AdminUsuariosListarComponent implements OnInit {
usuarios: any[] = [];
paginaActual: number = 0;
tamanioPagina: number = 5;
totalPaginas: number = 0;

constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.obtenerUsuarios();
  }
actualizarLista(): void {
  this.userService.getUsuariosPaginado(this.paginaActual, this.tamanioPagina).subscribe({
    next: res => {
      this.usuarios = res.content;
      this.totalPaginas = res.totalPages;
    },
    error: () => console.error('Error al cargar usuarios')
  });
}


  obtenerUsuarios(): void {
    this.userService.getUsuariosPaginado(this.paginaActual, this.tamanioPagina).subscribe({
      next: (res) => {
        this.usuarios = res.content;
        this.totalPaginas = res.totalPages;
      },
      error: (err) => {
        console.error('Error al obtener usuarios:', err);
      }
    });
  }

  siguientePagina(): void {
    if (this.paginaActual + 1 < this.totalPaginas) {
      this.paginaActual++;
      this.obtenerUsuarios();
    }
  }

  anteriorPagina(): void {
    if (this.paginaActual > 0) {
      this.paginaActual--;
      this.obtenerUsuarios();
    }
  }

  esActivo(usuario: any): boolean {
    return usuario.activo === true;
  }

  getEstadoTexto(usuario: any): string {
    return this.esActivo(usuario) ? 'Activo' : 'Inactivo';
  }

  getEstadoColor(usuario: any): string {
    return this.esActivo(usuario) ? 'green' : 'red';
  }

  darDeBaja(username: string): void {
    if (!confirm(`¿Estás seguro de dar de baja a ${username}?`)) return;

    this.userService.darDeBaja(username).subscribe({
      next: () => {
        alert(`${username} ha sido dado de baja correctamente`);
        this.obtenerUsuarios();
      },
      error: (err) => {
        console.error(`Error al dar de baja a ${username}:`, err);
        alert(`Error al dar de baja a ${username}: ${err.message || 'ver consola'}`);
      }
    });
  }
}
