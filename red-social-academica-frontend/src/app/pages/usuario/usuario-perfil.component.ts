import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; // 👈 IMPORTANTE
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-usuario-perfil',
  standalone: true,
  imports: [CommonModule, RouterModule], // 👈 AGREGA AQUÍ
  templateUrl: './usuario-perfil.component.html',
  styleUrls: ['./usuario-perfil.component.scss']
})
export class UsuarioPerfilComponent implements OnInit {
  perfil: any = null;
  error: string | null = null;
  imagenValida: boolean = true;
  cargando: boolean = true; // 🆕

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getPerfil().subscribe({
      next: (data: any) => {
        this.perfil = data;
        this.error = null;
        this.cargando = false;
      },
      error: () => {
        this.error = '❌ Error al cargar el perfil';
        this.cargando = false;
      }
    });
  }
}
