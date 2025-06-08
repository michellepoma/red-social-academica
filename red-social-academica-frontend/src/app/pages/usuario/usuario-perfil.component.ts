import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service'; // Asegúrate que la ruta es correcta
import { CommonModule } from '@angular/common';

@Component({
selector: 'app-usuario-perfil',
standalone: true,
imports: [CommonModule],
templateUrl: './usuario-perfil.component.html',
styleUrls: ['./usuario-perfil.component.scss']
})
export class UsuarioPerfilComponent implements OnInit {
perfil: any = null;
error: string | null = null;

constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getPerfil().subscribe({
      next: (data: any) => this.perfil = data,
      error: (err: any) => this.error = 'Error al cargar el perfil'
    });
  }
}
