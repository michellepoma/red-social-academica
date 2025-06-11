import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { UserService } from 'src/app/services/user.service'; // ✅ asegurate de importar esto
import { InvitationService } from 'src/app/services/invitation.service';
import { NotificationDTO } from 'src/app/dto/notification.dto';

@Component({
selector: 'app-home-usuario',
standalone: true,
imports: [CommonModule, RouterModule, FormsModule],
templateUrl: './home-usuario.component.html',
styleUrls: ['./home-usuario.component.scss']
})
export class HomeUsuarioComponent implements OnInit {
usuario: any = null;
amigos: any[] = [];
invitaciones: any[] = [];
comentariosPropios: any[] = [];
resultadoBusqueda: any = null;
busquedaUsername: string = '';
busquedaFallida: boolean = false;
cargando = true;
seccion: string = 'amigos';

// Comentarios
nuevoComentario = { postId: 0, content: '' };
comentarioEditando: any = null;
comentariosDePost: any[] = [];
postIdParaVerComentarios = 0;

//notificaciones
notificaciones: any[] = [];
notificacionesNoLeidas: any[] = [];
totalNoLeidas: number = 0;
ultimasNotis: any[] = [];
cantidadNoLeidas = 0;

constructor(
    private userService: UserService,
    private perfilService: UserService,
    private invitationService: InvitationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.perfilService.getPerfil().subscribe({
      next: (res) => {
        this.usuario = res;
        console.log('Usuario logueado:', this.usuario);
        this.cargando = false;
        this.cargarAmigos();
        this.cargarInvitaciones();
        this.cargarNotificaciones();
        this.cargarResumenNotificaciones();

      },
      error: () => this.router.navigate(['/login'])
    });
  }

  verSeccion(nombre: string): void {
    this.seccion = nombre;
    if (nombre === 'comentarios') {
      this.cargarMisComentarios();
    }
  }

  cargarAmigos(): void {
    this.perfilService.getAmigos().subscribe({
      next: (res) => this.amigos = res,
      error: (err) => console.error('Error al cargar amigos:', err)
    });
  }

  cargarInvitaciones(): void {
    const username = this.usuario?.username;
    console.log('Username para buscar invitaciones:', username);
    if (!username) return;

    this.perfilService.getInvitacionesPendientes(username).subscribe({
      next: (res) => {
        console.log('Invitaciones recibidas del backend:', res);
        this.invitaciones = res;
      },
      error: () => {
        console.error('Error al cargar invitaciones pendientes');
        this.invitaciones = [];
      }
    });
  }

  cargarMisComentarios(): void {
    const username = this.usuario?.username;
    if (!username) return;
    this.perfilService.getMisComentarios(username).subscribe({
      next: res => this.comentariosPropios = res,
      error: err => {
        console.error('Error al cargar mis comentarios:', err);
        this.comentariosPropios = [];
      }
    });
  }

  crearComentario(): void {
    const { postId, content } = this.nuevoComentario;
    if (!postId || !content.trim()) return;
    this.perfilService.crearComentario({ postId, content }).subscribe({
      next: () => {
        alert('Comentario creado.');
        this.nuevoComentario = { postId: 0, content: '' };
        this.cargarMisComentarios();
      },
      error: err => console.error('Error al crear comentario:', err)
    });
  }

  editarComentario(comentario: any): void {
    this.comentarioEditando = { ...comentario };
  }

  guardarEdicionComentario(): void {
    const { id, content } = this.comentarioEditando;
    this.perfilService.actualizarComentario(id, { content }).subscribe({
      next: () => {
        alert('Comentario actualizado.');
        this.comentarioEditando = null;
        this.cargarMisComentarios();
      },
      error: err => console.error('Error al editar comentario:', err)
    });
  }

  cancelarEdicion(): void {
    this.comentarioEditando = null;
  }

  eliminarComentario(id: number): void {
    const motivo = prompt('Motivo de eliminación:');
    if (!motivo) return;
    this.perfilService.eliminarComentario(id, motivo).subscribe({
      next: () => {
        alert('Comentario eliminado.');
        this.cargarMisComentarios();
      },
      error: err => console.error('Error al eliminar comentario:', err)
    });
  }

  verComentariosDePost(): void {
    if (!this.postIdParaVerComentarios) return;
    this.perfilService.getComentariosDePost(this.postIdParaVerComentarios).subscribe({
      next: res => this.comentariosDePost = res,
      error: err => console.error('Error al obtener comentarios del post:', err)
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  darDeBaja(): void {
    if (confirm('¿Estás seguro de que deseas darte de baja?')) {
      this.perfilService.eliminarMiCuenta().subscribe({
        next: () => {
          alert('Cuenta desactivada correctamente.');
          this.logout();
        },
        error: (err) => {
          console.error('Error al darse de baja:', err);
          alert('No se pudo desactivar la cuenta.');
        }
      });
    }
  }

  buscarUsuario(): void {
    const username = this.busquedaUsername.trim();
    if (!username) return;

    this.perfilService.getPerfilPublico(username).subscribe({
      next: (res) => {
        this.resultadoBusqueda = res;
        this.busquedaFallida = false;
      },
      error: (err) => {
        console.error('Usuario no encontrado:', err);
        this.resultadoBusqueda = null;
        this.busquedaFallida = true;
      }
    });
  }

  enviarInvitacion(destinatario: string): void {
    const remitente = this.usuario?.username;
    if (!remitente || !destinatario) {
      alert('Datos inválidos');
      return;
    }

    this.perfilService.enviarInvitacion(remitente, destinatario).subscribe({
      next: () => {
        alert('Invitación enviada correctamente.');
        this.resultadoBusqueda = null;
        this.busquedaUsername = '';
        this.cargarInvitaciones();
      },
      error: (err) => {
        console.error('Error al enviar invitación:', err);
        alert('No se pudo enviar la invitación.');
      }
    });
  }

  rechazarInvitacion(invitationId: number): void {
    const username = this.usuario?.username;
    if (!username) return;

    this.perfilService.rechazarInvitacion(invitationId, username).subscribe({
      next: () => {
        alert('Invitación rechazada.');
        this.invitaciones = this.invitaciones.filter(inv => inv.id !== invitationId);
      },
      error: (err: any) => {
        console.error('Error al rechazar invitación:', err);
        alert('No se pudo rechazar la invitación.');
      }
    });
  }

cargarNotificaciones(): void {
  this.perfilService.getNotificaciones().subscribe({
    next: res => this.notificaciones = res,
    error: err => console.error('Error al cargar notificaciones:', err)
  });

  this.perfilService.contarNoLeidas().subscribe({
    next: res => this.totalNoLeidas = res,
    error: err => console.error('Error al contar no leídas:', err)
  });
}

marcarComoLeida(id: number): void {
  this.perfilService.marcarNotificacionComoLeida(id).subscribe({
    next: () => {
      this.cargarNotificaciones();
      this.cargarResumenNotificaciones(); // <- agrega esto
    },
    error: err => console.error('Error al marcar como leída:', err)
  });
}


eliminarNoti(id: number): void {
  this.perfilService.eliminarNotificacion(id).subscribe({
    next: () => {
      this.cargarNotificaciones();
      this.cargarResumenNotificaciones(); // <- también aquí
    },
    error: err => console.error('Error al eliminar notificación:', err)
  });
}


 cargarResumenNotificaciones(): void {
    this.userService.getUltimasNoLeidas().subscribe({
      next: res => this.ultimasNotis = res.slice(0, 5),
      error: err => console.error('Error al obtener últimas notificaciones:', err)
    });

    this.userService.getCantidadNoLeidas().subscribe({
      next: res => this.cantidadNoLeidas = res,
      error: err => console.error('Error al contar no leídas:', err)
    });
  }
}






