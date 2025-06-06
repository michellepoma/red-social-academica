import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-admin-usuarios-listar',
  standalone: true,
  templateUrl: './admin-usuarios-listar.component.html',
  styleUrls: ['./admin-usuarios-listar.component.scss'],
  imports: [CommonModule]
})
export class AdminUsuariosListarComponent implements OnInit {
  usuarios: any[] = [];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe({
      next: (res: any[]) => this.usuarios = res,
      error: (err) => console.error('Error al obtener usuarios:', err)
    });
  }
obtenerUsuarios(): void {
  this.userService.getAllUsers().subscribe({
    next: res => {
      this.usuarios = res;
    },
    error: err => {
      console.error('Error al obtener usuarios:', err);
    }
  });
}


darDeBaja(username: string): void {
  if (!confirm(`¿Estás seguro de dar de baja a ${username}?`)) return;

  this.userService.darDeBaja(username).subscribe({
    next: res => {
      alert(`${username} ha sido dado de baja correctamente`);
      this.obtenerUsuarios(); // recargar la lista
    },
    error: err => {
      alert(`Error al dar de baja a ${username}: ${err.message}`);
      console.error(err);
    }
  });
}

}
