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

  darDeBaja(username: string): void {
    if (confirm(`¿Deseas dar de baja a ${username}?`)) {
      this.userService.darDeBaja(username).subscribe({
        next: () => this.ngOnInit(),
        error: (err) => alert('Error al dar de baja: ' + (err.error?.message || err.message))
      });
    }
  }
}
