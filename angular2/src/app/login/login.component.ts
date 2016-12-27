import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastyService, ToastyConfig, ToastOptions, ToastData } from 'ng2-toasty';

import { ApiService } from '../api.service';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

	password: string;
	toastOptions: ToastOptions;

	constructor(private router: Router,
		private apiService: ApiService,
		private toastyService: ToastyService,
		private toastyConfig: ToastyConfig) {
	}

	ngOnInit() {
		this.toastyConfig.theme = 'material';
		this.toastOptions = {
			title: 'Invalid Password',
			msg: '',
			showClose: false,
			timeout: 2000
		};
		this.password = '';
	}

	login() {
		if (this.password == '') {
			this.router.navigate(['admin']);
		} else {
			this.toastyService.error(this.toastOptions);
			this.password = '';
		}

		/*	this.apiService.login(this.password).subscribe(
				result => {
					if (result == 'success') {
						this.router.navigate(['admin']);
					}
				},
				error => console.log(error)
			);*/
	}

}
