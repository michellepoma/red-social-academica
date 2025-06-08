import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
selector: 'app-amigos',
standalone: true,
imports: [CommonModule, RouterModule],
templateUrl: './amigos.component.html',
styleUrls: ['./amigos.component.scss']
})
export class AmigosComponent implements OnInit {
amigos: any[] = [];
cargando = true;

constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getAmigos().subscribe({
      next: (res) => {
        this.amigos = res;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar amigos:', err);
        this.cargando = false;
      }
    });
  }
}
