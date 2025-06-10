import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';


import { PostService, Publicacion } from 'src/app/services/post.service';

@Component({
    selector: 'app-admin-publicaciones',
    standalone: true,
    templateUrl: './admin-publicaciones.component.html',
    styleUrls: ['./admin-publicaciones.component.scss'],
    imports: [CommonModule, RouterModule, FormsModule]
})
export class AdminPublicacionesComponent implements OnInit {
    publicaciones: Publicacion[] = [];
    publicacionesPaginadas: Publicacion[] = [];

    mensaje: string = '';
    username: string = '';
    textoBusqueda: string = '';

    paginaActual = 0;
    tamanioPagina = 6;
    totalPaginas = 0;

    constructor(
        private postService: PostService,
        private route: ActivatedRoute,
        private router: Router,
        private http: HttpClient
    ) { }


    ngOnInit(): void {
        const nombreUsuario = this.route.snapshot.paramMap.get('username');
        if (nombreUsuario) {
            this.username = nombreUsuario;
            this.cargarPublicaciones();
        } else {
            this.mensaje = '❌ No se pudo obtener el nombre del usuario.';
        }
    }

    cargarPublicaciones(): void {
        // 🟢 Carga con un tamaño suficientemente grande para traer todo
        this.postService.obtenerPublicacionesDeUsuario(this.username, 0, 1000).subscribe({
            next: (res) => {
                this.publicaciones = res.content ?? [];
                this.totalPaginas = Math.ceil(this.publicaciones.length / this.tamanioPagina);
                this.paginaActual = 0;
                this.actualizarVistaPaginada();
                this.mensaje = this.publicaciones.length === 0
                    ? 'Este usuario no tiene publicaciones activas.'
                    : '';
            },
            error: (err) => {
                console.error('Error al cargar publicaciones del usuario:', err);
                this.mensaje = '❌ Error al obtener publicaciones.';
            }
        });
    }

    actualizarVistaPaginada(): void {
        const inicio = this.paginaActual * this.tamanioPagina;
        const fin = inicio + this.tamanioPagina;
        this.publicacionesPaginadas = this.publicaciones.slice(inicio, fin);
    }

    siguientePagina(): void {
        if ((this.paginaActual + 1) < this.totalPaginas) {
            this.paginaActual++;
            this.actualizarVistaPaginada();
        }
    }

    anteriorPagina(): void {
        if (this.paginaActual > 0) {
            this.paginaActual--;
            this.actualizarVistaPaginada();
        }
    }

    buscar(): void {
        const texto = this.textoBusqueda.trim().toLowerCase();

        if (texto === '') {
            this.paginaActual = 0;
            this.totalPaginas = Math.ceil(this.publicaciones.length / this.tamanioPagina);
            this.actualizarVistaPaginada(); // mostrar todo de nuevo
            return;
        }

        const resultados = this.publicaciones.filter(pub =>
            (pub.title?.toLowerCase().includes(texto) || '') ||
            (pub.text?.toLowerCase().includes(texto) || '') ||
            (pub.resourceUrl?.toLowerCase().includes(texto) || '')
        );

        this.paginaActual = 0;
        this.totalPaginas = Math.ceil(resultados.length / this.tamanioPagina);
        this.publicacionesPaginadas = resultados.slice(0, this.tamanioPagina);
        this.mensaje = resultados.length === 0 ? 'No se encontraron coincidencias.' : '';
    }

    limpiarBusqueda(): void {
        this.textoBusqueda = '';
        this.paginaActual = 0;
        this.totalPaginas = Math.ceil(this.publicaciones.length / this.tamanioPagina);
        this.actualizarVistaPaginada(); // mostrar todas de nuevo
    }

    editarPublicacion(postId: number): void {
        this.router.navigate(['/admin/publicaciones/editar', postId]);
    }


    eliminarPublicacion(postId: number): void {
        // Si ya no quieres confirmación, elimina la siguiente línea
        if (!confirm('¿Estás seguro de eliminar esta publicación permanentemente?')) return;

        const motivo = 'eliminado_por_admin';

        this.http.delete(`/api/admin/posts/${postId}?motivo=${encodeURIComponent(motivo)}`).subscribe({
            next: () => {
                alert('✅ Publicación eliminada correctamente.');
                this.cargarPublicaciones(); // Recarga la lista actual
            },
            error: (err: any) => {
                console.error('Error al eliminar la publicación:', err);
                alert('❌ No se pudo eliminar la publicación.');
            }
        });
    }


}
