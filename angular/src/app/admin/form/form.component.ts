import { Component, Inject } from '@angular/core';
import { MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { Devotee } from '../../devotee';

@Component({
	selector: 'app-form',
	templateUrl: './form.component.html',
	styleUrls: ['./form.component.css']
})
export class FormComponent {

	devotee: Devotee;

	type: string;

	constructor(public dialog: MdDialogRef<FormComponent>,
		@Inject(MD_DIALOG_DATA) public data: Map<any, any>) {
		this.devotee = data['devotee'];
		this.type = data['type'];
	}

}
