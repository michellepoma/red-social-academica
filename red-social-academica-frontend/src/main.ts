// src/main.ts
import { enableProdMode }                         from '@angular/core';
import { bootstrapApplication }                   from '@angular/platform-browser';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient }                      from '@angular/common/http';
import { AppComponent }                           from './app/app.component';
import { routes }                                 from './app/app.routes';

// Si quieres habilitar production mode según tu entorno, descomenta lo siguiente:
// import { environment } from './environments/environment';
// if (environment.production) {
//   enableProdMode();
// }

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient()
  ]
})
.catch(err => console.error(err));
