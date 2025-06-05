import type { Routes } from '@angular/router';

// Componentes tradicionales (NO standalone)
import { LoginComponent }         from './pages/login/login.component';
import { SignupComponent }        from './pages/signup/signup.component';
import { HomeComponent }          from './pages/home/home.component';

// Admin “padre”
import { AdminComponent }         from './pages/admin/admin.component';

// Componentes de “admin-usuarios”
import { AdminUsuariosListarComponent }   from './pages/admin/admin-usuarios/admin-usuarios-listar.component';
import { AdminUsuariosCrearComponent }    from './pages/admin/admin-usuarios/admin-usuarios-crear.component';
import { AdminUsuariosEditarComponent }   from './pages/admin/admin-usuarios/admin-usuarios-editar.component';
import { AdminUsuariosBajaComponent }     from './pages/admin/admin-usuarios/admin-usuarios-baja.component';
import { AdminUsuariosPerfilComponent }   from './pages/admin/admin-usuarios/admin-usuarios-perfil.component';
import { AdminUsuariosPorRolComponent }   from './pages/admin/admin-usuarios/admin-usuarios-por-rol.component';
import { AdminUsuariosBuscarComponent }   from './pages/admin/admin-usuarios/admin-usuarios-buscar.component';

export const routes: Routes = [
  { path: '',        redirectTo: 'login', pathMatch: 'full' },
  { path: 'login',   component: LoginComponent },
  { path: 'signup',  component: SignupComponent },
  { path: 'home',    component: HomeComponent },

  {
    path: 'no-auth',
    loadComponent: () => import('./pages/no-autorizado/no-autorizado.component')
      .then(m => m.NoAutorizadoComponent)
  },

  {
    path: 'admin',
    component: AdminComponent,
    children: [
      { path: 'usuarios/listar', component: AdminUsuariosListarComponent },
      { path: 'usuarios/crear', component: AdminUsuariosCrearComponent },
      { path: 'usuarios/editar/:username', component: AdminUsuariosEditarComponent },
      { path: 'usuarios/baja/:username', component: AdminUsuariosBajaComponent },
      { path: 'usuarios/perfil/:username', component: AdminUsuariosPerfilComponent },
      { path: 'usuarios/por-rol/:role', component: AdminUsuariosPorRolComponent },
      { path: 'usuarios/buscar', component: AdminUsuariosBuscarComponent },
      { path: '', redirectTo: 'usuarios/listar', pathMatch: 'full' }
    ]
  },

  // Cualquier ruta no reconocida redirige a /no-auth
  { path: '**', redirectTo: 'no-auth' }
];
