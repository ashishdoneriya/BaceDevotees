import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpModule, JsonpModule } from '@angular/http';
import { MdlModule } from 'angular2-mdl';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminComponent
  ],
  imports: [
    BrowserModule,
	HttpModule,
    JsonpModule,
	MdlModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
