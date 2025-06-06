// src/app/app.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet
  ],
  template: `


    <!-- Aquí aparece el LoginComponent, SignupComponent, HomeComponent, etc. -->
    <router-outlet></router-outlet>
  `,
  styles: [
    `
    /* Puedes poner estilos globales aquí si quieres */
    body {
      margin: 0;
      font-family: Arial, sans-serif;
    }
    `
  ]
})
export class AppComponent { }
