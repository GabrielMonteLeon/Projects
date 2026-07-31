import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { Home } from './home/home';
import { Login } from './login/login';
import { Register } from './register/register';
import { Need } from './need/need';
import { ManagerList } from './manager-home/manager-home';
import { HelperHome } from './helper-home/helper-home';
import { CartHome } from './cart/cart';
import { CreateNeed } from './create-need/create-need';
import { EditNeed } from './edit-need/edit-need';
import { authGuard, managerGuard, helperGuard, editNeedGuard } from './auth.guard';


const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'need/:id', component: Need},
  { path: 'manager/:id', component: ManagerList, canActivate: [managerGuard] },
  { path: 'helper/:id', component: HelperHome, canActivate: [helperGuard] },
  { path: 'create/:id', component: CreateNeed},
  { path: 'edit/:id/:userId', component: EditNeed, canActivate: [editNeedGuard] },
  { path: 'cart/:id', component: CartHome, canActivate: [helperGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }