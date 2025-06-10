import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router'; // 👈 IMPORTA Router
import { FormsModule } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-editar-perfil',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    templateUrl: './editar-perfil.component.html'
})
export class EditarPerfilComponent implements OnInit {
    perfil: any = {
        name: '',
        lastName: '',
        email: '',
        career: '',
        bio: '',
        birthdate: ''
    };

    error: string | null = null;
    mensaje: string | null = null;

    imagenValida: boolean = true;

    constructor(private userService: UserService, private router: Router) { }

    ngOnInit(): void {
        this.error = null;
        this.mensaje = null;

        this.userService.getPerfil().subscribe({
            next: (data) => this.perfil = data,
            error: () => this.error = 'No se pudo cargar tu perfil.'
        });
    }


    guardar(): void {
        // Validación simple en frontend
        if (!this.perfil.name || !this.perfil.lastName || !this.perfil.email) {
            this.error = '⚠️ Por favor completa los campos obligatorios.';
            return;
        }

        if (!this.validarEmail(this.perfil.email)) {
            this.error = '📧 El correo no tiene un formato válido.';
            return;
        }

        // Reset de mensajes y spinner
        this.mensaje = '';
        this.error = '';
        this.imagenValida = true;

        this.userService.editarMiPerfil(this.perfil).subscribe({
            next: (res) => {
                console.log('Respuesta del backend:', res);
                if (res?.success === false || res?.error) {
                    const detalles = res?.detalles || res?.error || 'Error inesperado.';
                    this.error = typeof detalles === 'object'
                        ? Object.entries(detalles).map(([k, v]) => `${k}: ${v}`).join(' | ')
                        : detalles;
                } else {
                    this.mensaje = '✅ Perfil actualizado.';
                    setTimeout(() => this.router.navigate(['/usuario/perfil']), 1500);
                }
            },

            error: (err) => {
                const detalles = err.error?.detalles || err.error;

                if (typeof detalles === 'object') {
                    this.error = Object.entries(detalles)
                        .map(([campo, mensaje]) => `${campo}: ${mensaje}`)
                        .join(' | ');
                } else {
                    this.error = detalles || '❌ Error desconocido al actualizar.';
                }
            }
        });
    }
    validarEmail(email: string): boolean {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }


}
