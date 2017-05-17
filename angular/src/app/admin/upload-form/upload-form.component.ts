import { Component } from '@angular/core';
import { MdDialogRef, MD_DIALOG_DATA } from '@angular/material';
import { ApiService } from '../../api.service';

@Component({
	selector: 'upload-form',
	templateUrl: './upload-form.component.html',
	styleUrls: ['./upload-form.component.css']
})
export class UploadFormComponent {

	file: File;

	showProgress: boolean = false;

	constructor(public apiService: ApiService,
		public dialog: MdDialogRef<UploadFormComponent>) {
	}

	onChange(event: EventTarget) {
		let eventObj: MSInputMethodContext = <MSInputMethodContext>event;
		let target: HTMLInputElement = <HTMLInputElement>eventObj.target;
		let files: FileList = target.files;
		this.file = files[0];
	}

	upload() {
		this.showProgress = true;

		this.apiService.upload(this.file).subscribe(
			next => {
				this.dialog.close('success');
			},
			error => {
				this.dialog.close(error);
			},
			() => {
				console.log('complete');
				this.dialog.close('success');
			}
		);
	}

}
