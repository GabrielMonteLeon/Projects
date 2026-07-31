import { NgModule, provideBrowserGlobalErrorListeners} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Home } from './home/home';
import { Login } from './login/login';
import { Register } from './register/register';
import { Need } from './need/need';
import { ManagerList } from './manager-home/manager-home';
import { HttpClientModule } from '@angular/common/http';
import { CreateNeed } from './create-need/create-need';
import { FormsModule } from '@angular/forms';
import { HelperHome } from './helper-home/helper-home';
import { EditNeed } from './edit-need/edit-need';
import { CartHome } from './cart/cart';

@NgModule({
  declarations: [
    App,
    Home,
    Login,
    Register,
    Need,
    ManagerList,
    CreateNeed,
    HelperHome,
    EditNeed,
    CartHome
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    provideBrowserGlobalErrorListeners(),
  ],
  bootstrap: [App]
})
export class AppModule { }