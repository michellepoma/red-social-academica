import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-nueva-publicacion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './nueva-publicacion.component.html',
  styleUrls: ['./nueva-publicacion.component.scss']
})
export class NuevaPublicacionComponent {
  form: FormGroup;
  mensaje = '';
  error: string[] = [];
  previewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private router: Router
  ) {
    this.form = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
      text: ['', Validators.required],
      imageUrl: [''],
      resourceUrl: [''],
      eventDate: ['']
    });

    this.form.get('imageUrl')?.valueChanges.subscribe(() => this.actualizarPreview());
  }

  actualizarPreview(): void {
    const url = this.form.get('imageUrl')?.value;
    this.previewUrl = url && url.trim() !== '' ? url : null;
  }

  crear(): void {
    if (this.form.invalid) return;

    this.mensaje = '';
    this.error = [];

    console.log('Payload enviado:', this.form.value);

    this.postService.crearPublicacion(this.form.value).subscribe({
      next: () => {
        this.mensaje = '✅ Publicación creada exitosamente.';
        this.router.navigate(['/usuario/publicaciones/propias']);
      },
      error: (err) => {
        console.error('Error al crear publicación:', err);
        const detalles = err?.error?.detalles;

        if (detalles) {
          this.error = Object.values(detalles);
        } else if (err?.error?.mensaje) {
          this.error = [err.error.mensaje];
        } else {
          this.error = ['❌ Error desconocido al crear publicación.'];
        }
      }
    });
  }
}
