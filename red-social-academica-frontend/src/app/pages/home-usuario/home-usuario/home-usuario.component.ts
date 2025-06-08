import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
selector: 'app-home-usuario',
standalone: true,
imports: [CommonModule, RouterModule],
templateUrl: './home-usuario.component.html',
styleUrls: ['./home-usuario.component.scss']
})
export class HomeUsuarioComponent implements OnInit {
usuario: any = null;
amigos: any[] = [];
cargando = true;

constructor(
    private perfilService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.perfilService.getPerfil().subscribe({
      next: (res) => {
        this.usuario = res;
        this.cargando = false;
        this.cargarAmigos();
      },
      error: (err) => {
        console.error('Error al cargar perfil:', err);
        this.router.navigate(['/login']);
      }
    });
  }

  cargarAmigos(): void {
    this.perfilService.getAmigos().subscribe({
      next: (res) => {
        this.amigos = res;
      },
      error: (err) => {
        console.error('Error al cargar amigos:', err);
      }
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  darDeBaja(): void {
    if (confirm('¿Estás seguro de que deseas darte de baja?')) {
      this.perfilService.eliminarMiCuenta().subscribe({
        next: () => {
          alert('Cuenta desactivada correctamente.');
          this.logout();
        },
        error: (err) => {
          console.error('Error al darse de baja:', err);
          alert('No se pudo desactivar la cuenta.');
        }
      });
    }
  }
}
