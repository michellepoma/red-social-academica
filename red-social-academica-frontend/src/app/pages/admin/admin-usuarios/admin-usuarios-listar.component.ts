import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-admin-usuarios-listar',
  standalone: true,
  templateUrl: './admin-usuarios-listar.component.html',
  styleUrls: ['./admin-usuarios-listar.component.scss'],
  imports: [CommonModule, RouterModule, FormsModule]
})
export class AdminUsuariosListarComponent implements OnInit {
  usuarios: any[] = [];
  paginaActual: number = 0;
  tamanioPagina: number = 5;
  totalPaginas: number = 0;
  textoBusqueda: string = '';
  mensaje: string = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    if (this.textoBusqueda.trim()) {
      this.buscar();
    } else {
      this.obtenerUsuarios();
    }
  }

  buscar(): void {
    const textoNormalizado = this.textoBusqueda.trim().toUpperCase();
    this.userService.buscarUsuarios(textoNormalizado, this.paginaActual, this.tamanioPagina).subscribe({
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

  obtenerUsuarios(): void {
    this.userService.getUsuariosPaginado(this.paginaActual, this.tamanioPagina).subscribe({
      next: (res) => {
        this.usuarios = res.content;
        this.totalPaginas = res.totalPages;
        this.mensaje = '';
      },
      error: (err) => {
        console.error('Error al obtener usuarios:', err);
      }
    });
  }

  siguientePagina(): void {
    if (this.paginaActual + 1 < this.totalPaginas) {
      this.paginaActual++;
      this.cargarUsuarios();
    }
  }

  anteriorPagina(): void {
    if (this.paginaActual > 0) {
      this.paginaActual--;
      this.cargarUsuarios();
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
        this.cargarUsuarios();
      },
      error: (err) => {
        console.error(`Error al dar de baja a ${username}:`, err);
        alert(`Error al dar de baja a ${username}: ${err.message || 'ver consola'}`);
      }
    });
  }

  limpiarBusqueda(): void {
    this.textoBusqueda = '';
    this.paginaActual = 0;
    this.cargarUsuarios();
  }
}
