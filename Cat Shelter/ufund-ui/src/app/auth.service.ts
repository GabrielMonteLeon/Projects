import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUser: any = null;

  setUser(user: any) {
    this.currentUser = user;
  }

  getUser() {
    return this.currentUser;
  }

  isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  isManager(): boolean {
    return this.currentUser?.role === 'manager';
  }

  isHelper(): boolean {
    return this.currentUser?.role === 'helper';
  }

  logout() {
    this.currentUser = null;
  }
}