import { Component, EventEmitter, ViewChild } from '@angular/core';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
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
		public activeModal: NgbActiveModal) {
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
			next => {},
			error => {
				this.activeModal.close(error);
			},
			() => {
				console.log('complete');
				this.activeModal.close('success');
			}
		);
	}

}
