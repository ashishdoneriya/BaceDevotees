import { Component, OnInit } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Devotee } from '../devotee';
import { FormComponent } from './form/form.component';
import { ApiService } from '../api.service';

@Component({
	selector: 'app-admin',
	templateUrl: './admin.component.html',
	styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

	constructor(private apiService: ApiService,
		private router: Router,
		private modalService: NgbModal) { }
	devoteesList: Devotee[];

	ngOnInit() {
		this.devoteesList = [];
		let devotee: Devotee = new Devotee();
		devotee.name = 'Ashish Doneriya';
		this.devoteesList.push(devotee);
	}

	addDevotee() {
		const modalRef = this.modalService.open(FormComponent);
	}

	logout() {
		this.apiService.logout();
		this.router.navigate(['login']);
	}
}
