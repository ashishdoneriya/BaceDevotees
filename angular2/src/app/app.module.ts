import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }   from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import {ToastyModule} from 'ng2-toasty';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ApiService } from './api.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormComponent } from './admin/form/form.component';
import { UploadFormComponent } from './admin/upload-form/upload-form.component';

const appRoutes: Routes = [
	{ path: '', component: LoginComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'admin', component: AdminComponent }
]

@NgModule({
	declarations: [
		AppComponent,
		LoginComponent,
		AdminComponent,
		FormComponent,
		UploadFormComponent
	],
	imports: [
		BrowserModule,
		HttpModule,
		JsonpModule,
		FormsModule,
		NgbModule.forRoot(),
		ToastyModule.forRoot(),
		RouterModule.forRoot(appRoutes)
	],
	providers: [ApiService],
	bootstrap: [AppComponent],
	entryComponents: [FormComponent, UploadFormComponent]
})
export class AppModule { }
