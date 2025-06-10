import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from 'src/app/services/post.service';
import { CommonModule } from '@angular/common'; // 👈 ¡Importante!

@Component({
  selector: 'app-editar-publicacion',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule], // 👈 Asegúrate de incluir CommonModule
  templateUrl: './editar-publicacion.component.html',
  styleUrls: ['./editar-publicacion.component.scss']
})
export class EditarPublicacionComponent implements OnInit {
  form: FormGroup;
  mensaje = '';
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
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.postService.getPostById(this.postId).subscribe({
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

    this.postService.actualizarPublicacion(this.postId, this.form.value).subscribe({
      next: () => {
        this.mensaje = '✅ Publicación actualizada correctamente.';
        this.router.navigate(['/publicaciones/propias']);
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
}
