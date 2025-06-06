// src/app/pages/admin/admin-usuarios-buscar.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-usuarios-buscar',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <h3>Buscar Usuarios (Admin)</h3>
    <p>Aquí iría un campo para buscar usuarios por nombre o email.</p>
  `
})
export class AdminUsuariosBuscarComponent { }
