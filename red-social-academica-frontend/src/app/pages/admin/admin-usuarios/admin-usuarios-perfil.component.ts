// src/app/pages/admin/admin-usuarios-perfil.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-admin-usuarios-perfil',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <h3>Perfil de Usuario (Admin)</h3>
    <p>Ver perfil del usuario con username = "{{ username }}".</p>
  `
})
export class AdminUsuariosPerfilComponent {
  username: string | null;

  constructor(private route: ActivatedRoute) {
    this.username = this.route.snapshot.paramMap.get('username');
  }
}
