import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-perfil-publico',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './perfil-publico.component.html'
})
export class PerfilPublicoComponent implements OnInit {
  perfil: any = null;
  error: string | null = null;
  cargando: boolean = true;
  imagenValida = true;

  constructor(private route: ActivatedRoute, private userService: UserService) {}

  ngOnInit(): void {
    const username = this.route.snapshot.paramMap.get('username');
    if (username) {
      this.userService.getPerfilPublico(username).subscribe({
        next: (res) => {
          this.perfil = res;
          this.cargando = false;
        },
        error: () => {
          this.error = '❌ No se pudo cargar el perfil público.';
          this.cargando = false;
        }
      });
    } else {
      this.error = '❌ Nombre de usuario inválido.';
      this.cargando = false;
    }
  }
}
