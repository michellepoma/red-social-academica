// src/app/pages/admin/admin-usuarios/admin-usuarios-crear.component.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../../services/user.service';

@Component({
selector: 'app-admin-usuarios-crear',
standalone: true,
templateUrl: './admin-usuarios-crear.component.html',
styleUrls: ['./admin-usuarios-crear.component.scss'],
imports: [CommonModule, ReactiveFormsModule, RouterModule]
})
export class AdminUsuariosCrearComponent implements OnInit {
form: FormGroup;
error: string = '';
success: string = '';

constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    this.form = this.fb.group({
    name: ['', Validators.required],
    lastName: ['', Validators.required],
    username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    ru: ['', Validators.required],
    career: ['', Validators.required],
    bio: [''],
    birthdate: ['', Validators.required],
    profilePictureUrl: [''],
    password: ['', Validators.required],
    passwordConfirm: ['', Validators.required],
    rol: ['', Validators.required]
  });

  }

  ngOnInit(): void {}

  crearUsuario(): void {
    if (this.form.invalid) {
      this.error = 'Por favor, completa todos los campos.';
      return;
    }

    this.userService.crearUsuario(this.form.value).subscribe({
      next: () => {
        this.success = 'Usuario creado exitosamente.';
        this.form.reset();
        setTimeout(() => {
          this.router.navigate(['/admin/usuarios/listar']);
        }, 1000); // Redirige después de un segundo
      },
      error: (err) => {
        console.error('Error al crear usuario:', err);
        this.error = err.error?.message || 'Error al crear usuario.';
      }
    });
  }
}

