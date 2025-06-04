// src/app/guards/role.guard.ts
import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const expectedRole = route.data['role'] as string;
    if (!this.auth.hasRole(expectedRole)) {
      // Si no tiene el rol requerido, redirige a “no autorizado” o login
      this.router.navigate(['/no-autorizado']);
      return false;
    }
    return true;
  }
}
