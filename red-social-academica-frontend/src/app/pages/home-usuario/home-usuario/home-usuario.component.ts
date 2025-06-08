import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from 'src/app/services/auth.service';
import { NavbarUsuarioComponent } from 'src/app/pages/usuario/navbar-usuario/navbar-usuario.component';

@Component({
selector: 'app-home-usuario',
standalone: true,
templateUrl: './home-usuario.component.html',
styleUrls: ['./home-usuario.component.scss'],
imports: [CommonModule, NavbarUsuarioComponent]
})
export class HomeUsuarioComponent implements OnInit {
roles: string[] = [];

constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
  const roles = this.authService.getRoles();

  if (roles.includes('ROLE_ADMIN')) {
    this.router.navigate(['/admin/usuarios/listar']);
  }
}

}
