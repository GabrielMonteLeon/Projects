import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private router = inject(Router);
  private http = inject(HttpClient);
  private auth = inject(AuthService);

  username = '';
  password = '';

  login() {
    if (!this.username || !this.password) {
      alert('Please fill in all fields.');
      return;
    }

    this.http.post<any>('http://localhost:8080/users/login', {
      id: 0,
      name: this.username,
      password: this.password,
      role: 'user'
    }).subscribe({
      next: (user: any) => {
        this.auth.setUser(user);
        if(user.role === 'manager'){
          this.router.navigate(['/manager', user.id])
        }
        if(user.role === 'helper'){
          this.router.navigate(['/helper', user.id])
        }
      },
      error: (err: any) => {
        if (err.status === 401) alert('Incorrect password.');
        else if (err.status === 404) alert('User not found.');
        else alert('Login failed. Please try again.');
      }
    });
  }

  createAccount() {
    this.router.navigate(['/register']);
  }
}