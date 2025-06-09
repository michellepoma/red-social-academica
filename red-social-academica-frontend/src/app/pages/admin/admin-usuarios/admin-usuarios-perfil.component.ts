import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-admin-usuarios-perfil',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="container mt-4" *ngIf="usuario">
      <h3>👤 Perfil del Usuario</h3>
      <p><strong>Nombre:</strong> {{ usuario.name }} {{ usuario.lastName }}</p>
      <p><strong>Usuario:</strong> {{ usuario.username }}</p>
      <p><strong>Email:</strong> {{ usuario.email }}</p>
      <p><strong>RUT:</strong> {{ usuario.ru }}</p>
      <p><strong>Rol:</strong> {{ usuario.roles?.[0] }}</p>
      <p><strong>Estado:</strong> {{ usuario.activo ? 'Activo' : 'Inactivo' }}</p>
      <p><strong>Alta:</strong> {{ usuario.fechaAlta }}</p>
      <p *ngIf="usuario.fechaModificacion"><strong>Última modificación:</strong> {{ usuario.fechaModificacion }}</p>
      <p *ngIf="usuario.motivoBaja"><strong>Motivo de baja:</strong> {{ usuario.motivoBaja }}</p>
    </div>
    <div *ngIf="!usuario && cargando">Cargando...</div>
    <div *ngIf="error" class="alert alert-danger">{{ error }}</div>
  `
})
export class AdminUsuariosPerfilComponent implements OnInit {
  username: string | null = null;
  usuario: any;
  error: string = '';
  cargando = true;

  constructor(private route: ActivatedRoute, private userService: UserService) {}

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username');
    if (this.username) {
      this.userService.getPerfilUsuario(this.username).subscribe({
        next: (res) => {
          this.usuario = res;
          this.cargando = false;
        },
        error: (err) => {
          console.error('Error al obtener perfil:', err);
          this.error = 'No se pudo cargar el perfil del usuario.';
          this.cargando = false;
        }
      });
    }
  }
}
