// ================================
// invitaciones-enviadas.component.ts
// ================================
import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router'; // ✅ FALTA




@Component({
standalone: true,
selector: 'app-invitaciones-enviadas',
imports: [CommonModule, RouterModule], // 👈 IMPORTAR AQUI
templateUrl: './invitaciones-enviadas.component.html',
styleUrls: ['./invitaciones-enviadas.component.scss']
})
export class InvitacionesEnviadasComponent implements OnInit {
enviadas: any[] = [];

constructor(private userService: UserService) {}

  ngOnInit(): void {
    const username = localStorage.getItem('username');
    if (username) {
      this.userService.getInvitacionesEnviadas(username).subscribe(res => this.enviadas = res);
    }
  }

  cancelar(id: number): void {
    const username = localStorage.getItem('username');
    if (!username) return;
    this.userService.cancelarInvitacion(id, username).subscribe(() => {
      this.enviadas = this.enviadas.filter(inv => inv.id !== id);
    });
  }
}
