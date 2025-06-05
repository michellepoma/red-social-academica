// src/app/pages/home/home.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ CommonModule ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  roles: string[] = [];

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.roles = this.authService.getRoles();

    // Si tiene el rol ADMIN, redirige al módulo de administración
    if (this.roles.includes('ADMIN')) {
      this.router.navigate(['/admin/usuarios/listar']);
    }
  }
}
