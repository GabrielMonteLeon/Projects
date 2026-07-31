import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  const urlId = Number(route.paramMap.get('id'));
  if (urlId && auth.getUser().id !== urlId) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};

export const managerGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isLoggedIn() || !auth.isManager()) {
    router.navigate(['/login']);
    return false;
  }

  const urlId = Number(route.paramMap.get('id'));
  if (urlId && auth.getUser().id !== urlId) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};

export const helperGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isLoggedIn() || !auth.isHelper()) {
    router.navigate(['/login']);
    return false;
  }

  const urlId = Number(route.paramMap.get('id'));
  if (urlId && auth.getUser().id !== urlId) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};

export const editNeedGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
 
  if (!auth.isLoggedIn() || !auth.isManager()) {
    router.navigate(['/login']);
    return false;
  }
 
  const userId = Number(route.paramMap.get('userId'));
  if (userId && auth.getUser().id !== userId) {
    router.navigate(['/login']);
    return false;
  }
 
  return true;
};