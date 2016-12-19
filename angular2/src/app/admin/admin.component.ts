import { Component, OnInit } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { Devotee } from '../devotee';

@Component({
	selector: 'app-admin',
	templateUrl: './admin.component.html',
	styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

	constructor(private modalService: NgbModal) { }

	devoteeList: Devotee[];

	ngOnInit() {
		this.devoteeList = [];
		let devotee: Devotee = new Devotee();
		devotee.name = 'Ashish Doneriya';
		this.devoteeList.push(devotee);
	}

	addDevotee() {
	}
}
