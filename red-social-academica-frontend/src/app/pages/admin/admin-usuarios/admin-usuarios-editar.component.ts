// src/app/pages/admin/admin-usuarios-editar.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-admin-usuarios-editar',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <h3>Editar Usuario (Admin)</h3>
    <p>Editar datos del usuario con username = "{{ username }}".</p>
  `
})
export class AdminUsuariosEditarComponent {
  username: string | null;

  constructor(private route: ActivatedRoute) {
    this.username = this.route.snapshot.paramMap.get('username');
  }
}
