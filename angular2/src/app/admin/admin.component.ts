import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef, NgbModalOptions, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';

import { Devotee } from '../devotee';
import { FormComponent } from './form/form.component';
import { ApiService } from '../api.service';

@Component({
	selector: 'app-admin',
	templateUrl: './admin.component.html',
	styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

	devoteesList: Devotee[];
	searchQuery: string;

	constructor(private apiService: ApiService,
		private router: Router,
		private modalService: NgbModal) { }


	ngOnInit() {
		this.updateList();
	}

	search() {
		this.apiService.list(this.searchQuery).subscribe(data => this.devoteesList = data);
	}

	add() {
		let devotee: Devotee = new Devotee();
		let modelOption: NgbModalOptions = { backdrop: false, keyboard: true };
		let modalRef: NgbModalRef = this.modalService.open(FormComponent, modelOption);
		modalRef.componentInstance.devotee = devotee;
		modalRef.componentInstance.type = "Add";
		modalRef.result.then(result => {
			if (result == 'save') {
				this.apiService.save(devotee).subscribe(message => console.log(message));
			}
		});
	}

	edit(devotee: Devotee) {
		let modelOption: NgbModalOptions = { backdrop: false, keyboard: true };
		let modalRef: NgbModalRef = this.modalService.open(FormComponent, modelOption);
		modalRef.componentInstance.devotee = devotee;
		modalRef.componentInstance.type = "Update";
		modalRef.result.then(result => {
			if (result == 'save') {
				this.apiService.save(devotee).subscribe(message => console.log(message));
			}
		});
	}

	delete(id) {
		this.apiService.delete(id);
	}

	logout() {
		this.apiService.logout();
		this.router.navigate(['login']);
	}

	updateList() {
		this.apiService.list().subscribe( list => this.devoteesList = list);
	}
}
