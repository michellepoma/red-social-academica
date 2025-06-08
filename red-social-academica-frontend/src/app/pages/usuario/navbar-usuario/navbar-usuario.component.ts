import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from 'src/app/services/auth.service';

@Component({
selector: 'app-navbar-usuario',
standalone: true,
templateUrl: './navbar-usuario.component.html',
styleUrls: ['./navbar-usuario.component.scss'],
imports: [CommonModule]
})
export class NavbarUsuarioComponent {
constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}

