import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { PostService, Publicacion } from 'src/app/services/post.service';

@Component({
  selector: 'app-admin-editar-publicacion',
  standalone: true,
  templateUrl: './admin-editar-publicacion.component.html',
  imports: [CommonModule, ReactiveFormsModule]
})
export class AdminEditarPublicacionComponent implements OnInit {
  form: FormGroup;
  mensaje: string = '';
  error: string[] = [];
  postId!: number;
  previewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private postService: PostService
  ) {
    this.form = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      text: ['', Validators.required],
      imageUrl: [''],
      resourceUrl: [''],
      eventDate: ['']
    });
  }

  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.paramMap.get('postId'));
    this.cargarPublicacion();
  }

  cargarPublicacion(): void {
    this.postService.obtenerPostPorIdAdmin(this.postId).subscribe({
      next: (post) => {
        this.form.patchValue(post);
        this.previewUrl = post.imageUrl || null;
      },
      error: () => {
        this.error = ['❌ No se pudo cargar la publicación.'];
      }
    });
  }

  actualizarPreview(): void {
    const url = this.form.get('imageUrl')?.value;
    this.previewUrl = url?.trim() ? url : null;
  }

  guardar(): void {
    if (this.form.invalid) return;

    this.postService.actualizarPostAdmin(this.postId, this.form.value).subscribe({
      next: () => {
        this.mensaje = '✅ Publicación actualizada correctamente.';
        this.router.navigate(['/admin/usuarios/listar']);
      },
      error: (err) => {
        const detalles = err?.error?.detalles;
        if (detalles) {
          this.error = Object.values(detalles);
        } else if (err?.error?.mensaje) {
          this.error = [err.error.mensaje];
        } else {
          this.error = ['❌ Error desconocido.'];
        }
      }
    });
  }
  cancelar(): void {
  this.router.navigate(['/admin/usuarios/listar']);
}

}
