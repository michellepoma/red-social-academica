import { Component, OnInit } from '@angular/core';
import { InvitationService } from 'src/app/services/invitation.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-pendientes',
  templateUrl: './pendientes.component.html',
  styleUrls: ['./pendientes.component.scss']
})
export class PendientesComponent implements OnInit {
  usuarioUsername: string = '';
  invitaciones: any[] = [];

  constructor(
    private invitationService: InvitationService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const usuario = this.authService.getUsuario(); // Ajustalo si usás otro método
    this.usuarioUsername = usuario?.username || '';

    if (this.usuarioUsername) {
      this.invitationService
        .obtenerPendientesRecibidas(this.usuarioUsername)
        .subscribe(data => {
          this.invitaciones = data;
        });
    }
  }

  aceptarInvitacion(id: number) {
    this.invitationService.aceptarInvitacion(id).subscribe(() => {
      this.invitaciones = this.invitaciones.filter(i => i.id !== id);
    });
  }

  rechazarInvitacion(id: number) {
    this.invitationService.rechazarInvitacion(id).subscribe(() => {
      this.invitaciones = this.invitaciones.filter(i => i.id !== id);
    });
  }
}
