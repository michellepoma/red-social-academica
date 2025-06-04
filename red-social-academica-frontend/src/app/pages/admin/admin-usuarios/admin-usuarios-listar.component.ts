// src/app/pages/admin/admin-usuarios/admin-usuarios-listar.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-usuarios-listar',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <h3>Listar Usuarios (Admin)</h3>
    <p>Aquí iría la tabla de usuarios para el administrador.</p>
  `
})
export class AdminUsuariosListarComponent { }
