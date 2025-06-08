import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from 'src/app/services/auth.service';
import { AdminNavbarComponent } from 'src/app/pages/admin/admin-navbar/admin-navbar.component';

@Component({
selector: 'app-home-admin',
standalone: true,
templateUrl: './home-admin.component.html',
styleUrls: ['./home-admin.component.scss'],
imports: [CommonModule, AdminNavbarComponent]
})
export class HomeAdminComponent implements OnInit {
roles: string[] = [];

constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.roles = this.authService.getRoles() || [];

    if (!this.roles.includes('ROLE_ADMIN')) {
      this.router.navigate(['/home']);
    }
  }
}
