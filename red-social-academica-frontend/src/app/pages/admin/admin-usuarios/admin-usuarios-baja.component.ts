// src/app/pages/admin/admin-usuarios-baja.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-admin-usuarios-baja',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <h3>Dar de Baja Usuario (Admin)</h3>
    <p>Dar de baja al usuario con username = "{{ username }}".</p>
  `
})
export class AdminUsuariosBajaComponent {
  username: string | null;

  constructor(private route: ActivatedRoute) {
    this.username = this.route.snapshot.paramMap.get('username');
  }
}
