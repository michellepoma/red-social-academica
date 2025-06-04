// src/app/pages/admin/admin-usuarios-por-rol.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-admin-usuarios-por-rol',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <h3>Usuarios por Rol (Admin)</h3>
    <p>Listado de usuarios con rol = "{{ role }}".</p>
  `
})
export class AdminUsuariosPorRolComponent {
  role: string | null;

  constructor(private route: ActivatedRoute) {
    this.role = this.route.snapshot.paramMap.get('role');
  }
}
