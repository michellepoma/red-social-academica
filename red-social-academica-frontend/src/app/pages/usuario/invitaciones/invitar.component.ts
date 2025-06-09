// ================================
// invitar.component.ts
// ================================
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

@Component({
selector: 'app-invitar',
imports: [CommonModule, ReactiveFormsModule, FormsModule], // 👈 AÑADE ESTO
templateUrl: './invitar.component.html',
styleUrls: ['./invitar.component.scss']
})
export class InvitarComponent {
form: FormGroup;
mensaje: string | null = null;

constructor(private fb: FormBuilder, private userService: UserService) {
    this.form = this.fb.group({
      receiverUsername: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  enviarInvitacion(): void {
    if (this.form.invalid) return;
    const sender = localStorage.getItem('username');
    if (!sender) return;

    this.userService.enviarInvitacion(sender, this.form.value.receiverUsername).subscribe({
      next: () => this.mensaje = 'Invitación enviada exitosamente.',
      error: err => {
        console.error(err);
        this.mensaje = 'Error al enviar invitación.';
      }
    });
  }
}
