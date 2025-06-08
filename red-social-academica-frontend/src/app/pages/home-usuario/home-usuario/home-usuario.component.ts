import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';

@Component({
selector: 'app-home-usuario',
standalone: true,
imports: [CommonModule, RouterModule, FormsModule],
templateUrl: './home-usuario.component.html',
styleUrls: ['./home-usuario.component.scss']
})
export class HomeUsuarioComponent implements OnInit {
usuario: any = null;
amigos: any[] = [];
cargando = true;

busquedaUsername: string = '';
resultadoBusqueda: any = null;
busquedaFallida: boolean = false;

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

  buscarUsuario(): void {
    const username = this.busquedaUsername.trim();
    if (!username) return;

    this.perfilService.getPerfilPublico(username).subscribe({
      next: (res) => {
        this.resultadoBusqueda = res;
        this.busquedaFallida = false;
      },
      error: (err) => {
        console.error('Usuario no encontrado:', err);
        this.resultadoBusqueda = null;
        this.busquedaFallida = true;
      }
    });
  }

  enviarInvitacion(destinatario: string): void {
    const remitente = this.usuario?.username;
    if (!remitente || !destinatario) return;

    this.perfilService.enviarInvitacion(remitente, destinatario).subscribe({
      next: () => {
        alert('Invitación enviada.');
        this.resultadoBusqueda = null;
        this.busquedaUsername = '';
      },
      error: (err) => {
        console.error('Error al enviar invitación:', err);
        alert('No se pudo enviar la invitación.');
      }
    });
  }
}
