// src/app/pages/admin-navbar/admin-navbar.component.ts
import { Component }   from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-navbar',
  standalone: true,
  imports: [ RouterModule ],
  templateUrl: './admin-navbar.component.html',
  styleUrls: ['./admin-navbar.component.scss']
})
export class AdminNavbarComponent {}
