import { Routes } from '@angular/router';

// Componentes públicos
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { HomeUsuarioComponent } from './pages/home-usuario/home-usuario/home-usuario.component';
import { UsuarioPerfilComponent } from './pages/usuario/usuario-perfil.component';
import { AmigosComponent } from './pages/usuario/amigos.component';

// Guardas de acceso
import { AuthGuard } from './guards/auth.guard'; // ✅

// Componentes administrativos
import { HomeAdminComponent } from './pages/home-admin/home-admin/home-admin.component';
import { AdminComponent } from './pages/admin/admin.component';
import { AdminUsuariosListarComponent } from './pages/admin/admin-usuarios/admin-usuarios-listar.component';
import { AdminUsuariosCrearComponent } from './pages/admin/admin-usuarios/admin-usuarios-crear.component';
import { AdminUsuariosEditarComponent } from './pages/admin/admin-usuarios/admin-usuarios-editar.component';
import { AdminUsuariosBajaComponent } from './pages/admin/admin-usuarios/admin-usuarios-baja.component';
import { AdminUsuariosPerfilComponent } from './pages/admin/admin-usuarios/admin-usuarios-perfil.component';

// Ruta no autorizada
import { NoAutorizadoComponent } from './pages/no-autorizado/no-autorizado.component';

export const routes: Routes = [
{ path: '', redirectTo: 'login', pathMatch: 'full' },

// Públicas
{ path: 'login', component: LoginComponent },
{ path: 'signup', component: SignupComponent },
{ path: 'home', component: HomeUsuarioComponent },
{ path: 'perfil', component: UsuarioPerfilComponent },
{ path: 'amigos', component: AmigosComponent },

// Rutas de usuario protegidas con AuthGuard
{
path: 'usuario',
canActivate: [AuthGuard],
children: [
{
path: 'invitaciones',
loadComponent: () => import('./pages/usuario/invitaciones/invitar.component').then(m => m.InvitarComponent)
      },
      {
        path: 'invitaciones/recibidas',
        loadComponent: () => import('./pages/usuario/invitaciones/invitaciones-recibidas.component').then(m => m.InvitacionesRecibidasComponent)
      },
      {
        path: 'invitaciones/enviadas',
        loadComponent: () => import('./pages/usuario/invitaciones/invitaciones-enviadas.component').then(m => m.InvitacionesEnviadasComponent)
      }
    ]
  },

  // Home exclusivo para administrador
  { path: 'admin/home', component: HomeAdminComponent },

  // Panel de administración
  {
    path: 'admin',
    component: AdminComponent,
    children: [
      { path: '', redirectTo: 'usuarios/listar', pathMatch: 'full' },
      { path: 'usuarios/listar', component: AdminUsuariosListarComponent },
      { path: 'usuarios/crear', component: AdminUsuariosCrearComponent },
      { path: 'usuarios/editar/:username', component: AdminUsuariosEditarComponent },
      { path: 'usuarios/baja/:username', component: AdminUsuariosBajaComponent },
      { path: 'usuarios/perfil/:username', component: AdminUsuariosPerfilComponent }
    ]
  },

  // Ruta para acceso denegado
  { path: 'no-auth', component: NoAutorizadoComponent },

  // Ruta por defecto (cualquier otra)
  { path: '**', redirectTo: 'no-auth' }
];
