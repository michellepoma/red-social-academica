// Ruta: src/app/guards/role.guard.ts

import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router
} from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    // En el config de la ruta, esperamos data.role = 'ROLE_ADMIN', por ejemplo
    const expectedRole = route.data['role'] as string;
    if (!this.authService.hasRole(expectedRole)) {
      // Si no tiene el rol requerido, lo enviamos a /no-autorizado
      this.router.navigate(['/no-autorizado']);
      return false;
    }
    return true;
  }
}
