import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }   from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ApiService } from './api.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

const appRoutes: Routes = [
	{ path: '', component: LoginComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'admin', component: AdminComponent }
]

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
		FormsModule,
		NgbModule.forRoot(),
		RouterModule.forRoot(appRoutes)
	],
	providers: [ApiService],
	bootstrap: [AppComponent]
})
export class AppModule { }
