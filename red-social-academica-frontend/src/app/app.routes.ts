import { Routes } from '@angular/router';

// Componentes públicos
import { LoginComponent }   from './pages/login/login.component';
import { SignupComponent }  from './pages/signup/signup.component';
import { HomeComponent }    from './pages/home/home.component';

// Componentes administrativos
import { AdminComponent } from './pages/admin/admin.component';
import { AdminUsuariosListarComponent } from './pages/admin/admin-usuarios/admin-usuarios-listar.component';
import { AdminUsuariosCrearComponent }  from './pages/admin/admin-usuarios/admin-usuarios-crear.component';
import { AdminUsuariosEditarComponent } from './pages/admin/admin-usuarios/admin-usuarios-editar.component';
import { AdminUsuariosBajaComponent }   from './pages/admin/admin-usuarios/admin-usuarios-baja.component';
import { AdminUsuariosPerfilComponent } from './pages/admin/admin-usuarios/admin-usuarios-perfil.component';
import { AdminUsuariosPorRolComponent } from './pages/admin/admin-usuarios/admin-usuarios-por-rol.component';
import { AdminUsuariosBuscarComponent } from './pages/admin/admin-usuarios/admin-usuarios-buscar.component';

// Componente para rutas no autorizadas
import { NoAutorizadoComponent } from './pages/no-autorizado/no-autorizado.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'home', component: HomeComponent },

  // Rutas para panel de administración
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      { path: '', redirectTo: 'usuarios/listar', pathMatch: 'full' },
      { path: 'usuarios/listar', component: AdminUsuariosListarComponent },
      { path: 'usuarios/crear', component: AdminUsuariosCrearComponent },
      { path: 'usuarios/editar/:username', component: AdminUsuariosEditarComponent },
      { path: 'usuarios/baja/:username', component: AdminUsuariosBajaComponent },
      { path: 'usuarios/perfil/:username', component: AdminUsuariosPerfilComponent },
      { path: 'usuarios/por-rol/:role', component: AdminUsuariosPorRolComponent },
      { path: 'usuarios/buscar', component: AdminUsuariosBuscarComponent }
    ]
  },

  // Ruta para acceso denegado
  { path: 'no-auth', component: NoAutorizadoComponent },

  // Ruta por defecto para errores
  { path: '**', redirectTo: 'no-auth' }
];
