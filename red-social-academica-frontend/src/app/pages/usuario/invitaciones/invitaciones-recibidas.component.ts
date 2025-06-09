import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // ✅ IMPORTAR
import { UserService } from 'src/app/services/user.service';

@Component({
standalone: true,
selector: 'app-invitaciones-recibidas',
imports: [CommonModule], // ✅ AÑADIR AQUÍ
templateUrl: './invitaciones-recibidas.component.html',
styleUrls: ['./invitaciones-recibidas.component.scss']
})
export class InvitacionesRecibidasComponent implements OnInit {
invitaciones: any[] = [];

constructor(private userService: UserService) {}

  ngOnInit(): void {
    const username = localStorage.getItem('username');
    if (!username) return;

    this.userService.getInvitacionesPendientes(username).subscribe({
      next: (res) => (this.invitaciones = res),
      error: (err) => console.error('Error al obtener invitaciones:', err)
    });
  }

  aceptar(invitationId: number): void {
  const username = localStorage.getItem('username'); // o usa this.usuario.username
  if (!username) return;

  this.userService.aceptarInvitacion(invitationId, username).subscribe({
    next: () => {
      this.invitaciones = this.invitaciones.filter(i => i.id !== invitationId);
      alert('Invitación aceptada');
    },
    error: (err) => {
      console.error('Error al aceptar invitación', err);
      alert('No se pudo aceptar la invitación');
    }
  });
}

rechazar(invitationId: number): void {
  const username = localStorage.getItem('username');
  if (!username) return;

  this.userService.rechazarInvitacion(invitationId, username).subscribe({
    next: () => {
      this.invitaciones = this.invitaciones.filter(i => i.id !== invitationId);
      alert('Invitación rechazada');
    },
    error: (err) => {
      console.error('Error al rechazar invitación', err);
      alert('No se pudo rechazar la invitación');
    }
  });
}

}
