import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MdDialog } from '@angular/material';
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

import { ApiService } from '../api.service';
import { Devotee } from '../devotee';
import { FormComponent } from './form/form.component';
import { UploadFormComponent } from './upload-form/upload-form.component';

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
		private toastyService: ToastyService,
		private toastyConfig: ToastyConfig,
		public dialog: MdDialog) {
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
		let dialogRef = this.dialog.open(FormComponent, {
			height: '400px',
			width: '600px',
			data: {
				'devotee': devotee,
				'type': 'Add'
			}
		});
		dialogRef.afterClosed().subscribe(result => {
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
		});
	}

	edit(devotee: Devotee) {
		let temp = this.copy(devotee);
		let dialogRef = this.dialog.open(FormComponent, {
			height: '400px',
			width: '600px',
			data: {
				'devotee': temp,
				'type': 'Update'
			}
		});
		dialogRef.afterClosed().subscribe(result => {
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

	upload() {
		let dialogRef = this.dialog.open(UploadFormComponent, {
			height: '345px',
			width: '600px'
		});
		dialogRef.afterClosed().subscribe(result => {
			if (result == 'success') {
				this.toastOptions.title = '';
				this.toastOptions.msg = 'Records have been added successfully';
				this.toastyService.success(this.toastOptions);
				this.search();
			} else if (result) {
				this.toastOptions.title = 'Error while adding Records';
				this.toastOptions.msg = result;
				this.toastyService.error(this.toastOptions);
			}
		}, error => {
			if (error) {
				this.toastOptions.title = 'Error while adding Records';
				this.toastOptions.msg = error;
				this.toastyService.error(this.toastOptions);
			}
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
