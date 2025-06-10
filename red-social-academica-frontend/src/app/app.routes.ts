import { Routes } from '@angular/router';

// Componentes públicos
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { HomeUsuarioComponent } from './pages/home-usuario/home-usuario/home-usuario.component';
import { UsuarioPerfilComponent } from './pages/usuario/usuario-perfil.component';
import { AmigosComponent } from './pages/usuario/amigos.component';
import { NotificacionesComponent } from './pages/usuario/notificaciones.component';

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

// Rutas de usuario protegidas
{
path: 'usuario',
component: HomeUsuarioComponent,
canActivate: [AuthGuard],
children: [
{
path: 'publicaciones',
redirectTo: 'publicaciones/propias',
pathMatch: 'full'
},
{
path: 'invitaciones',
loadComponent: () =>
          import('./pages/usuario/invitaciones/invitaciones-recibidas.component').then(
            m => m.InvitacionesRecibidasComponent
)
},
{
path: 'notificaciones',
component: NotificacionesComponent
},
{
path: 'publicaciones/propias',
loadComponent: () =>
          import('./pages/usuario/publicaciones/mis-publicaciones.component').then(
            m => m.MisPublicacionesComponent
)
},
{
path: 'publicaciones/nueva',
loadComponent: () =>
          import('./pages/usuario/publicaciones/nueva-publicacion.component').then(
            m => m.NuevaPublicacionComponent
)
},
{
path: 'publicaciones/editar/:id',
loadComponent: () =>
          import('./pages/usuario/publicaciones/editar-publicacion.component').then(
            m => m.EditarPublicacionComponent
)
},
{
path: 'publicaciones/comentarios/:postId',
loadComponent: () =>
          import('./pages/usuario/comentarios/listar-comentarios.component').then(
            m => m.MisComentariosComponent
)
},
{ path: 'perfil', component: UsuarioPerfilComponent },
{ path: 'amigos', component: AmigosComponent },

{ path: '', redirectTo: 'invitaciones/recibidas', pathMatch: 'full' }
]
},

// Admin
{ path: 'admin/home', component: HomeAdminComponent },
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

// Denegado y fallback
{ path: 'no-auth', component: NoAutorizadoComponent },
{ path: '**', redirectTo: 'no-auth' }
];
