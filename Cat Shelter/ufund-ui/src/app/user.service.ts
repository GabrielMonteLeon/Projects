import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = 'http://localhost:8080/users';

  constructor(private http: HttpClient) {}

  register(name: string, password: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, {
      id: 0,
      name,
      password,
      role: 'user'
    });
  }
}