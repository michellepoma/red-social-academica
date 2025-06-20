import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from 'src/app/services/auth.service';

@Component({
selector: 'app-admin-navbar',
standalone: true,
templateUrl: './admin-navbar.component.html',
styleUrls: ['./admin-navbar.component.scss'],
imports: [CommonModule]
})
export class AdminNavbarComponent {
constructor(
    public auth: AuthService,
    private router: Router
  ) {}

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  esAdmin(): boolean {
    return this.auth.hasRole('ROLE_ADMIN');
  }
}
