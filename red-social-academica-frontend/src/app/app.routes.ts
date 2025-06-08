import { Routes } from '@angular/router';

// Componentes públicos
import { LoginComponent } from './pages/login/login.component';
import { SignupComponent } from './pages/signup/signup.component';
import { HomeUsuarioComponent } from './pages/home-usuario/home-usuario/home-usuario.component';
import { UsuarioPerfilComponent } from './pages/usuario/usuario-perfil.component';

// Componentes administrativos
import { HomeAdminComponent } from './pages/home-admin/home-admin/home-admin.component';
import { AdminComponent } from './pages/admin/admin.component';
import { AdminUsuariosListarComponent } from './pages/admin/admin-usuarios/admin-usuarios-listar.component';
import { AdminUsuariosCrearComponent } from './pages/admin/admin-usuarios/admin-usuarios-crear.component';
import { AdminUsuariosEditarComponent } from './pages/admin/admin-usuarios/admin-usuarios-editar.component';
import { AdminUsuariosBajaComponent } from './pages/admin/admin-usuarios/admin-usuarios-baja.component';
import { AdminUsuariosPerfilComponent } from './pages/admin/admin-usuarios/admin-usuarios-perfil.component';
import { AdminUsuariosBuscarComponent } from './pages/admin/admin-usuarios/admin-usuarios-buscar.component';
import { AdminUsuariosPorRolComponent } from './pages/admin/admin-usuarios/admin-usuarios-por-rol.component';

// Ruta no autorizada
import { NoAutorizadoComponent } from './pages/no-autorizado/no-autorizado.component';

export const routes: Routes = [
{ path: '', redirectTo: 'login', pathMatch: 'full' },

// Públicas
{ path: 'login', component: LoginComponent },
{ path: 'signup', component: SignupComponent },
{ path: 'home', component: HomeUsuarioComponent },
{ path: 'perfil', component: UsuarioPerfilComponent },

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
{ path: 'usuarios/perfil/:username', component: AdminUsuariosPerfilComponent },
{ path: 'usuarios/buscar', component: AdminUsuariosBuscarComponent },
{ path: 'usuarios/por-rol', component: AdminUsuariosPorRolComponent }
]
}
,

// Ruta para acceso denegado
{ path: 'no-auth', component: NoAutorizadoComponent },

// Ruta por defecto (cualquier otra)
{ path: '**', redirectTo: 'no-auth' }
];
