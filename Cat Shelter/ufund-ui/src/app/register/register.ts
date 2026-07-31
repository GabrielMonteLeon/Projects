import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  username = '';
  password = '';
  role: string = 'helper';

  constructor(private http: HttpClient, private router: Router) {}

  createNewAccount() {
    if (!this.username || !this.password) {
      alert('Please fill in all fields.');
      return;
    }

    this.http.post<any>('http://localhost:8080/users/register', {
      id: 0,
      name: this.username,
      password: this.password,
      role: this.role
    }).subscribe({
      next: (user: any) => {
        alert(`Account created! Welcome, ${user.name}`);
        this.router.navigate(['/login']);
      },
      error: (err: any) => {
        if (err.status === 409) alert('Username already taken.');
        else alert('Registration failed. Please try again.');
      }
    });
  }

  login() {
    this.router.navigate(['/login']);
  }
}