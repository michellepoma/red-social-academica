// src/app/pages/login/login.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
selector: 'app-login',
standalone: true,
imports: [
CommonModule,
ReactiveFormsModule,
RouterModule
],
templateUrl: './login.component.html',
styleUrls: ['./login.component.scss']
})
export class LoginComponent {
form: FormGroup;
loading = false;
errorMsg = '';

constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.errorMsg = 'Debes completar ambos campos.';
      return;
    }

    this.loading = true;
    this.errorMsg = '';

    this.auth.login(this.form.value).subscribe({
      next: res => {
        this.auth.saveToken(res.token);
        const roles = this.auth.getRoles();

        // Redirección basada en roles
        if (roles.includes('ADMIN')) {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/home']);
        }
      },
      error: err => {
        this.errorMsg = err.error?.message || 'Credenciales inválidas';
        this.loading = false;
      }
    });
  }
}
