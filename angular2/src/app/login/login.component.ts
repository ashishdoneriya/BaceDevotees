import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../api.service';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

	password: string;

	constructor(private router: Router, private apiService: ApiService) { }

	ngOnInit() {
		this.password = '';
	}

	login() {

		this.router.navigate(['admin']);
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
