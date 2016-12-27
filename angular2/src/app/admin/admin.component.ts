import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';

import "rxjs/add/operator/debounceTime";
import "rxjs/add/operator/distinctUntilChanged";
import "rxjs/add/operator/switchMap";

import { ToastyService, ToastyConfig, ToastOptions, ToastData } from 'ng2-toasty';
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
	searchQuery: string = '';
	currentPageNo: number = 1;
	totalResults: number = 0;
	maximumResults: number = 10;
	sortBy: string = 'name';
	order: string = 'ascending';

	cSerialNumber: boolean = true;
	cName: boolean = true;
	cbaceJoinDate: boolean = true;
	cbaceLeftDate: boolean = false;
	ccurrentAddress: boolean = false;
	cdob: boolean = true;
	cemail: boolean = true;
	cemergencyNumber: boolean = false;
	cfatherName: boolean = false;
	cmobileNumber: boolean = true;
	cpermanentAddress: boolean = false;
	cActions: boolean = true;

	toastOptions: ToastOptions;

	private searchTermStream = new Subject<string>();

	constructor(private apiService: ApiService,
		private router: Router,
		private modalService: NgbModal,
		private toastyService: ToastyService,
		private toastyConfig: ToastyConfig) {
	}

	updateSortOrder(property) {
		if (this.sortBy == property) {
			if (this.order == 'ascending') {
				this.order = 'descending';
			} else {
				this.order = 'ascending';
			}
		} else {
			this.sortBy = property;
			this.order = 'ascending';
		}
		this.search();
	}

	ngOnInit() {
		this.toastyConfig.theme = 'material';
		this.toastOptions = {
			title: '',
			msg: '',
			showClose: true,
			timeout: 3000
		};
		this.searchTermStream
			.debounceTime(300)
			.distinctUntilChanged().subscribe((term: string) => {
				this.currentPageNo = 1;
				this.search();
			});
		this.search();
	}

	update(query: string) {
		this.searchTermStream.next(query);
	}

	search(pageNumber?: number) {
		if (pageNumber) {
			this.currentPageNo = pageNumber;
		}
		this.apiService.list(this.searchQuery, this.currentPageNo, this.maximumResults, this.sortBy, this.order).subscribe(data => {
			let obj = data.json();
			this.totalResults = obj.totalResults;
			this.devoteesList = obj.records;
		}, error => {
			this.toastOptions.msg = 'Unable to fetch data.';
			this.toastyService.error(this.toastOptions);
		});
	}

	add() {
		let devotee: Devotee = new Devotee();
		let modelOption: NgbModalOptions = { backdrop: false, keyboard: true };
		let modalRef: NgbModalRef = this.modalService.open(FormComponent, modelOption);
		modalRef.componentInstance.devotee = devotee;
		modalRef.componentInstance.type = "Add";
		modalRef.result.then(result => {
			if (result == 'save') {
				this.apiService.save(devotee).subscribe(message => {
					this.toastOptions.title = 'Record Added';
					this.toastyService.success(this.toastOptions);
					this.search();
				}, error => {
					this.toastOptions.msg = 'Unable to add the record.';
					this.toastyService.error(this.toastOptions);
				});
			}
		}, (reason) => {
		});
	}

	edit(devotee: Devotee) {
		let modelOption: NgbModalOptions = { backdrop: false, keyboard: true };
		let modalRef: NgbModalRef = this.modalService.open(FormComponent, modelOption);
		let temp = this.copy(devotee);
		modalRef.componentInstance.devotee = temp;
		modalRef.componentInstance.type = "Update";
		modalRef.result.then(result => {
			if (result == 'save') {
				this.apiService.save(temp).subscribe(message => {
					this.toastOptions.title = 'Record Updated';
					this.toastyService.success(this.toastOptions);
					this.search();
				}, error => {
					this.toastOptions.msg = 'Unable to update the record.';
					this.toastyService.error(this.toastOptions);
				});
			}
		}, (reason) => {
		});
	}

	delete(id) {
		this.apiService.delete(id).subscribe(() => {
			this.toastOptions.title = 'Record Deleted';
			this.toastyService.success(this.toastOptions);
			this.search();
		}, error => {
			this.toastOptions.msg = 'Unable to delete the record.';
			this.toastyService.error(this.toastOptions);
		});
	}

	logout() {
		this.apiService.logout();
		this.router.navigate(['login']);
	}

	copy(devotee: Devotee): Devotee {
		let newDevotee: Devotee = new Devotee();
		newDevotee.id = devotee.id;
		newDevotee.name = devotee.name;
		newDevotee.baceJoinDate = devotee.baceJoinDate;
		newDevotee.baceLeftDate = devotee.baceLeftDate;
		newDevotee.currentAddress = devotee.currentAddress;
		newDevotee.dob = devotee.dob;
		newDevotee.email = devotee.email;
		newDevotee.emergencyNumber = devotee.emergencyNumber;
		newDevotee.fatherName = devotee.fatherName;
		newDevotee.mobileNumber = devotee.mobileNumber;
		newDevotee.permanentAddress = devotee.permanentAddress;
		return newDevotee;
	}

}
