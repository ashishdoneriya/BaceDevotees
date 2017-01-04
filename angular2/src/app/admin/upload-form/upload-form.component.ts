import { Component, OnInit, EventEmitter, ViewChild } from '@angular/core';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ApiService } from '../../api.service';

@Component({
	selector: 'upload-form',
	templateUrl: './upload-form.component.html',
	styleUrls: ['./upload-form.component.css']
})
export class UploadFormComponent implements OnInit {

	@ViewChild('form') form: HTMLFormElement;

	progress: number = 0;

	showProgress: boolean = false;

	constructor(private apiService: ApiService,
		public activeModal: NgbActiveModal) { }

	ngOnInit() {
	}

	upload() {
		this.showProgress = true;
		this.apiService.upload(this.form).subscribe(
			next => {
				if (typeof next == 'number') {
					this.progress = next;
				} else {
					this.activeModal.close(next);
				}
			},
			error => {
				this.activeModal.close(error);
			},
			() => {
				this.activeModal.close();
			}
		);
	}

}
