import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../services/user.service';

@Component({
selector: 'app-admin-usuarios-editar',
standalone: true,
templateUrl: './admin-usuarios-editar.component.html',
styleUrls: ['./admin-usuarios-editar.component.scss'],
imports: [
CommonModule,
ReactiveFormsModule,
RouterModule
]
})
export class AdminUsuariosEditarComponent implements OnInit {
form!: FormGroup;
username!: string;
error = '';

constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username')!;
    this.form = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      career: [''],
      bio: [''],
      profilePictureUrl: [''],
      birthdate: ['']
    });

    this.userService.getUsuarioPorUsername(this.username).subscribe({
      next: (usuario) => this.form.patchValue(usuario),
      error: () => this.error = 'No se pudo cargar el usuario.'
    });
  }

  editar(): void {
    if (this.form.invalid) return;

    this.userService.editarUsuario(this.username, this.form.value).subscribe({
      next: () => {
        alert('Usuario actualizado correctamente');
        this.router.navigate(['/admin/usuarios/listar']);
      },
      error: () => {
        this.error = 'Error al actualizar el usuario.';
      }
    });
  }
}
