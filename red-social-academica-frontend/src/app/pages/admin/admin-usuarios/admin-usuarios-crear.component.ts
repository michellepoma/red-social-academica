import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
selector: 'app-admin-usuarios-crear',
templateUrl: './admin-usuarios-crear.component.html',
standalone: true,
imports: [CommonModule, ReactiveFormsModule]
})
export class AdminUsuariosCrearComponent implements OnInit {
form!: FormGroup;
error: string = '';
success: string = '';

constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      ru: ['', Validators.required],
      profilePictureUrl: [''],
      bio: [''],
      career: ['', Validators.required],
      birthdate: ['', Validators.required],
      password: ['', Validators.required],
      passwordConfirm: ['', Validators.required],
      rol: ['ROLE_PUBLIC', Validators.required]
    });
  }

crearUsuario() {
  if (this.form.invalid) return;

  this.userService.crearUsuario(this.form.value).subscribe({
    next: () => {
      alert('Usuario creado exitosamente');
      this.router.navigate(['/admin/usuarios/listar']);
    },
    error: (err) => {
      console.error('Error al crear usuario:', err);
      this.error = 'Error al crear usuario';
    }
  });
}


}
