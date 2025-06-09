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
invitaciones: any[] = [];
resultadoBusqueda: any = null;
busquedaUsername: string = '';
busquedaFallida: boolean = false;
cargando = true;
seccion: string = 'amigos';

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
        this.cargarInvitaciones();
      },
      error: () => this.router.navigate(['/login'])
    });
  }

  verSeccion(nombre: string): void {
    this.seccion = nombre;
  }

  cargarAmigos(): void {
    this.perfilService.getAmigos().subscribe({
      next: (res) => this.amigos = res,
      error: (err) => console.error('Error al cargar amigos:', err)
    });
  }

  cargarInvitaciones(): void {
    const username = this.usuario?.username;
    if (!username) return;

    this.perfilService.getInvitacionesPendientes(username).subscribe({
      next: (res) => this.invitaciones = res,
      error: () => {
        console.error('Error al cargar invitaciones pendientes');
        this.invitaciones = [];
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
    if (!remitente || !destinatario) {
      alert('Datos inválidos');
      return;
    }

    this.perfilService.enviarInvitacion(remitente, destinatario).subscribe({
      next: () => {
        alert('Invitación enviada correctamente.');
        this.resultadoBusqueda = null;
        this.busquedaUsername = '';
        this.cargarInvitaciones(); // ⬅ recarga por si se autoenvía
      },
      error: (err) => {
        console.error('Error al enviar invitación:', err);
        alert('No se pudo enviar la invitación.');
      }
    });
  }

  aceptarInvitacion(invitationId: number): void {
    const username = this.usuario?.username;
    if (!username) return;

    this.perfilService.aceptarInvitacion(invitationId, username).subscribe({
      next: () => {
        alert('Invitación aceptada.');
        this.invitaciones = this.invitaciones.filter(inv => inv.id !== invitationId);
        this.cargarAmigos();
      },
      error: (err) => {
        console.error('Error al aceptar invitación:', err);
        alert('No se pudo aceptar la invitación.');
      }
    });
  }

  rechazarInvitacion(invitationId: number): void {
    const username = this.usuario?.username;
    if (!username) return;

    this.perfilService.rechazarInvitacion(invitationId, username).subscribe({
      next: () => {
        alert('Invitación rechazada.');
        this.invitaciones = this.invitaciones.filter(inv => inv.id !== invitationId);
      },
      error: (err) => {
        console.error('Error al rechazar invitación:', err);
        alert('No se pudo rechazar la invitación.');
      }
    });
  }
}

