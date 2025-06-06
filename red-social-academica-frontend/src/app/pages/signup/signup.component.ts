// src/app/pages/signup/signup.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent {
  form: FormGroup;
  cargando = false;
  mensajeError = '';

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      ru: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      career: ['', Validators.required],
      birthdate: ['', Validators.required],
      bio: [''],
      password: ['', Validators.required],
      passwordConfirm: ['', Validators.required],
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.mensajeError = 'Por favor completa todos los campos obligatorios.';
      return;
    }

    if (this.form.value.password !== this.form.value.passwordConfirm) {
      this.mensajeError = 'Las contraseñas no coinciden.';
      return;
    }

    this.cargando = true;
    this.mensajeError = '';

    const payload = this.form.value;

    this.auth.signup(payload).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        const detalles = err.error?.detalles || err.error;

        if (typeof detalles === 'object') {
          this.mensajeError = Object.entries(detalles)
            .map(([campo, mensaje]) => `${campo}: ${mensaje}`)
            .join(' | ');
        } else {
          this.mensajeError = detalles || 'Error desconocido en el registro.';
        }

        this.cargando = false;
      },
    });
  }
}
