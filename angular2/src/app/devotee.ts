export class Devotee {
	id?: string;
	name: string;
	dob?: Date;
	fatherName?: string;
	permanentAddress?: string;
	currentAddress?: string;
	mobileNumber?: string;
	email?: string;
	emergencyNumber?: string;
	baceJoinDate?: Date;
	baceLeftDate?: Date;

	constructor() {
		this.name = '';
		this.fatherName = '';
		this.permanentAddress = '';
		this.currentAddress = '';
		this.mobileNumber = '';
		this.email = '';
		this.emergencyNumber = '';
	}
}
