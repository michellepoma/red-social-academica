import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from 'src/app/services/user.service';

@Component({
selector: 'app-notificaciones',
standalone: true,
imports: [CommonModule],
templateUrl: './notificaciones.component.html'
})
export class NotificacionesComponent implements OnInit {
notificaciones: any[] = [];
totalNoLeidas = 0;

constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.cargarNotificaciones();
  }

  cargarNotificaciones(): void {
    this.userService.getNotificaciones().subscribe({
      next: res => this.notificaciones = res,
      error: err => console.error('Error al obtener notificaciones:', err)
    });

    this.userService.getCantidadNoLeidas().subscribe({
      next: res => this.totalNoLeidas = res,
      error: err => console.error('Error al contar no leídas:', err)
    });
  }

  marcarComoLeida(id: number): void {
    this.userService.marcarNotificacionComoLeida(id).subscribe(() => this.cargarNotificaciones());
  }

  eliminarNoti(id: number): void {
    this.userService.eliminarNotificacion(id).subscribe(() => this.cargarNotificaciones());
  }
}
