import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from 'src/app/services/auth.service';
import { NavbarUsuarioComponent } from '../usuario/navbar-usuario/navbar-usuario.component';

@Component({
selector: 'app-home',
standalone: true,
templateUrl: './home.component.html',
styleUrls: ['./home.component.scss'],
imports: [CommonModule, NavbarUsuarioComponent]
})
export class HomeComponent implements OnInit {
roles: string[] = [];

constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.roles = this.authService.getRoles() || [];

    if (this.roles.includes('ROLE_ADMIN')) {
      this.router.navigate(['/admin/usuarios/listar']);
    }
  }
}
