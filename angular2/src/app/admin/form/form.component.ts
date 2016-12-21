import { Component, OnInit, Input } from '@angular/core';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Devotee } from '../../devotee';
@Component({
	selector: 'app-form',
	templateUrl: './form.component.html',
	styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {

	@Input() devotee: Devotee;

	@Input() type: string;

	constructor(public activeModal: NgbActiveModal) {}

	ngOnInit() {
	}

}
