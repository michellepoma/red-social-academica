// src/app/pages/admin/admin.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [ CommonModule, RouterOutlet ],
  template: `
    <div class="container mt-4">
      <h2>Panel de Administración</h2>
      <hr />
      <!-- Aquí anidamos las rutas hijas de “admin” -->
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [
    `
    .container {
      max-width: 1000px;
    }
    `
  ]
})
export class AdminComponent { }
