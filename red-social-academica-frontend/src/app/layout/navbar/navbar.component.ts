import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service'; // Asegurate que la ruta sea correcta
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
ultimasNotis: any[] = [];
cantidadNoLeidas: number = 0;

constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.cargarNotificaciones();
  }

  cargarNotificaciones(): void {
    this.userService.getUltimasNoLeidas().subscribe({
      next: res => this.ultimasNotis = res,
      error: err => console.error('Error al cargar notificaciones', err)
    });

    this.userService.contarNoLeidas().subscribe({
      next: res => this.cantidadNoLeidas = res,
      error: err => console.error('Error al contar no leídas', err)
    });
  }
}
