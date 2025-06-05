import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-admin-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-navbar.component.html',
  styleUrls: ['./admin-navbar.component.scss']
})
export class AdminNavbarComponent {
  constructor(public auth: AuthService) {}

  logout() {
    localStorage.removeItem('token');
    window.location.href = '/login';
  }

  esAdmin(): boolean {
    return this.auth.hasRole('ADMIN');
  }
}
