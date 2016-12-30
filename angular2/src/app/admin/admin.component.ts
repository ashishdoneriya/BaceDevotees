import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import "rxjs/add/operator/debounceTime";
import "rxjs/add/operator/distinctUntilChanged";
import "rxjs/add/operator/switchMap";

import {
	ToastyService,
	ToastyConfig,
	ToastOptions,
	ToastData
} from 'ng2-toasty';

import {
	NgbModal,
	NgbModalRef,
	NgbModalOptions,
	ModalDismissReasons
} from '@ng-bootstrap/ng-bootstrap';

import { Devotee } from '../devotee';
import { FormComponent } from './form/form.component';
import { ApiService } from '../api.service';

export class Column {
	name: string;
	id: string;
	isSelected: boolean;
}

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
	cActions: boolean = true;
	columnsList: Array<Column> = [
		{
			name: "Name",
			id: "name",
			isSelected: true
		},
		{
			name: "Mobile Number",
			id: "mobileNumber",
			isSelected: true
		},
		{
			name: "Email",
			id: "email",
			isSelected: true
		},
		{
			name: "Father's Name",
			id: "fatherName",
			isSelected: false
		},
		{
			name: "Emergency Number",
			id: "emergencyNumber",
			isSelected: false
		},
		{
			name: "Current Address",
			id: "currentAddress",
			isSelected: false
		},
		{
			name: "Permanent Address",
			id: "permanentAddress",
			isSelected: false
		},
		{
			name: "Date of Birth",
			id: "dob",
			isSelected: true
		},
		{
			name: "Bace Join Date",
			id: "baceJoinDate",
			isSelected: true
		},
		{
			name: "Bace Left Date",
			id: "baceLeftDate",
			isSelected: false
		}
	];

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
			this.toastOptions.title = '';
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
					this.toastOptions.msg = '';
					this.toastyService.success(this.toastOptions);
					this.search();
				}, error => {
					this.toastOptions.title = '';
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
					this.toastOptions.msg = '';
					this.toastyService.success(this.toastOptions);
					this.search();
				}, error => {
					this.toastOptions.title = '';
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
			this.toastOptions.msg = '';
			this.toastyService.success(this.toastOptions);
			this.search();
		}, error => {
			this.toastOptions.title = '';
			this.toastOptions.msg = 'Unable to delete the record.';
			this.toastyService.error(this.toastOptions);
		});
	}

	logout() {
		this.apiService.logout();
		this.router.navigate(['login']);
	}

	download() {
		if (this.devoteesList == null || this.devoteesList.length == 0) {
			this.toastOptions.title = '';
			this.toastOptions.msg = 'No data to download.';
			this.toastyService.info(this.toastOptions);
			return;
		}
		let selectedColumns: Array<string> = this.getSelectedColumns();
		if (selectedColumns.length == 0) {
			this.toastOptions.title = '';
			this.toastOptions.msg = 'Please select atleast one field.';
			this.toastyService.info(this.toastOptions);
			return;
		}
		this.apiService
			.download(this.searchQuery, this.sortBy, this.order, selectedColumns)
			.subscribe(
			data => window.open(data.url),
			error => {
				this.toastOptions.title = 'Unable to download';
				this.toastOptions.msg = error;
				this.toastyService.error(this.toastOptions);
			});
	}

	copy(devotee: Devotee): Devotee {
		let newDevotee: Devotee = new Devotee();
		newDevotee.id = devotee.id;
		for (let column of this.columnsList) {
			newDevotee[column.id] = devotee[column.id];
		}
		return newDevotee;
	}

	getSelectedColumns(): Array<string> {
		let selectedColumns: Array<string> = [];
		for (let column of this.columnsList) {
			if (column.isSelected) {
				selectedColumns.push(column.name);
			}
		}
		return selectedColumns;
	}
}
