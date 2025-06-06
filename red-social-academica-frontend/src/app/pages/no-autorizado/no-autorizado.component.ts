// src/app/pages/no-autorizado/no-autorizado.component.ts
import { Component }    from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router }       from '@angular/router';

@Component({
  selector: 'app-no-autorizado',
  standalone: true,
  imports: [ CommonModule ],
  templateUrl: './no-autorizado.component.html',
  styleUrls: ['./no-autorizado.component.scss']
})
export class NoAutorizadoComponent {
  constructor(private router: Router) {}

  goLogin() {
    this.router.navigate(['/login']);
  }
}
