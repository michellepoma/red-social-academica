import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { InvitationService } from 'src/app/services/invitation.service';

@Component({
  selector: 'app-invitaciones-recibidas',
  standalone: true,
  templateUrl: './invitaciones-recibidas.component.html',
  styleUrls: ['./invitaciones-recibidas.component.scss'],
  imports: [CommonModule, FormsModule]
})
export class InvitacionesRecibidasComponent implements OnInit {
  username: string = '';
  seccion: string = 'recibidas';

  invitacionesRecibidas: any[] = [];
  invitacionesPendientes: any[] = [];
  invitacionesEnviadas: any[] = [];

  estadoLocal: { [id: number]: string } = {};
  procesando: { [id: number]: boolean } = {};

  nuevoDestinatario: string = '';
  mensaje: string = '';
  errores: string[] = [];

  constructor(
    private invitationService: InvitationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const usuario = this.authService.getUsuario();
    this.username = usuario?.username || usuario?.sub || '';

    if (this.username) {
      this.estadoLocal = {};
      this.cambiar('recibidas');
    }
  }

  cambiar(nombre: string): void {
    this.mensaje = '';
    this.errores = [];
    this.seccion = nombre;

    switch (nombre) {
      case 'recibidas':
        this.invitationService.obtenerTodasRecibidas(this.username).subscribe({
          next: data => {
            this.invitacionesRecibidas = data
              .filter(inv => inv.activo)
              .map(inv => {
                const estado = this.mapEstado(inv);
                this.estadoLocal[inv.id] = estado;
                return { ...inv, status: estado };
              });
          },
          error: err => {
            console.error('Error al cargar recibidas:', err);
            this.invitacionesRecibidas = [];
          }
        });
        break;

      case 'pendientes':
        this.invitationService.obtenerPendientesRecibidas(this.username).subscribe({
          next: data => {
            this.invitacionesPendientes = data
              .filter(inv => inv.activo)
              .map(inv => ({
                ...inv,
                status: this.mapEstado(inv)
              }));
          },
          error: err => {
            console.error('Error al cargar pendientes:', err);
            this.invitacionesPendientes = [];
          }
        });
        break;

      case 'enviadas':
        this.invitationService.obtenerInvitacionesEnviadas(this.username).subscribe({
          next: data => {
            this.invitacionesEnviadas = data
              .filter(inv => inv.activo)
              .map(inv => ({
                ...inv,
                status: this.mapEstado(inv)
              }));
          },
          error: err => {
            console.error('Error al cargar enviadas:', err);
            this.invitacionesEnviadas = [];
          }
        });
        break;
    }
  }

  aceptar(invitationId: number): void {
    this.procesando[invitationId] = true;

    this.invitationService.aceptarInvitacion(invitationId, this.username).subscribe({
      next: () => {
        alert('✅ Invitación aceptada');
        this.refrescarListas();
      },
      error: err => {
        const msg = err?.error?.detalles || '❌ Error al aceptar la invitación';
        alert(`⚠️ ${msg}`);
        console.error(`Error al aceptar invitación ${invitationId}`, err);
      },
      complete: () => {
        this.procesando[invitationId] = false;
      }
    });
  }

  rechazar(invitationId: number): void {
    this.procesando[invitationId] = true;

    this.invitationService.rechazarInvitacion(invitationId, this.username).subscribe({
      next: () => {
        alert('❌ Invitación rechazada');
        this.refrescarListas();
      },
      error: err => {
        const msg = err?.error?.detalles || '⚠️ Error al rechazar la invitación';
        alert(`⚠️ ${msg}`);
        console.error(`Error al rechazar invitación ${invitationId}`, err);
      },
      complete: () => {
        this.procesando[invitationId] = false;
      }
    });
  }

  cancelar(id: number): void {
  this.resetMensajes();
  this.invitationService.cancelarInvitacion(id, this.username).subscribe({
    next: () => {
      alert('❌ Invitación cancelada');
      this.cambiar('enviadas'); // 👈 vuelve a llamar al backend y se actualiza el listado
    },
    error: err => this.procesarError(err, 'Error al cancelar invitación')
  });
}


  enviarInvitacion(): void {
    this.resetMensajes();
    if (!this.nuevoDestinatario) return;

    const dto = { receiverUsername: this.nuevoDestinatario };
    this.invitationService.enviarInvitacion(this.username, dto).subscribe({
      next: () => {
        alert('✅ Invitación enviada');
        this.nuevoDestinatario = '';
        this.cambiar('enviadas');
      },
      error: err => this.procesarError(err, 'No se pudo enviar la invitación')
    });
  }

  private refrescarListas(): void {
    this.invitationService.obtenerTodasRecibidas(this.username).subscribe({
      next: data => {
        this.invitacionesRecibidas = data
          .filter(inv => inv.activo)
          .map(inv => ({
            ...inv,
            status: this.mapEstado(inv)
          }));
      },
      error: err => {
        console.error('Error al recargar invitaciones recibidas:', err);
        this.invitacionesRecibidas = [];
      }
    });

    this.invitationService.obtenerPendientesRecibidas(this.username).subscribe({
      next: data => {
        this.invitacionesPendientes = data
          .filter(inv => inv.activo)
          .map(inv => ({
            ...inv,
            status: this.mapEstado(inv)
          }));
      },
      error: err => {
        console.error('Error al recargar pendientes:', err);
        this.invitacionesPendientes = [];
      }
    });
  }

  private mapEstado(inv: any): string {
    if (!inv.activo && inv.motivoBaja?.toLowerCase() === 'aceptada') return 'ACEPTADA';
    if (!inv.activo && inv.motivoBaja?.toLowerCase() === 'rechazada') return 'RECHAZADA';
    if (!inv.activo && inv.motivoBaja?.toLowerCase().includes('cancelada')) return 'CANCELADA';
    if (inv.activo) return 'PENDIENTE';
    return 'DESCONOCIDO';
  }

  private procesarError(err: any, fallback: string): void {
    console.error(fallback, err);
    const detalles = err?.error?.detalles;
    if (detalles && typeof detalles === 'string') {
      this.errores = [detalles];
    } else if (typeof err?.error?.mensaje === 'string') {
      this.errores = [err.error.mensaje];
    } else {
      this.errores = [fallback];
    }
  }

  private resetMensajes(): void {
    this.mensaje = '';
    this.errores = [];
  }
}
