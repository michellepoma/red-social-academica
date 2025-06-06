// src/app/pages/admin/admin-usuarios-crear.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-usuarios-crear',
  standalone: true,
  imports: [ CommonModule ],
  template: `
    <h3>Crear Usuario (Admin)</h3>
    <p>Aquí iría el formulario para que el administrador cree un usuario.</p>
  `
})
export class AdminUsuariosCrearComponent { }
