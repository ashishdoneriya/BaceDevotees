export class Devotee {
	id?: string;
	name: string;
	baceJoinDate?: String;
	baceLeftDate?: String;
	currentAddress?: string;
	dob?: String;
	email?: string;
	emergencyNumber?: string;
	fatherName?: string;
	mobileNumber?: string;
	permanentAddress?: string;

	constructor() {
		this.name = '';
		this.baceJoinDate = '';
		this.baceLeftDate = '';
		this.currentAddress = '';
		this.dob = '';
		this.email = '';
		this.emergencyNumber = '';
		this.fatherName = '';
		this.mobileNumber = '';
		this.permanentAddress = '';

	}
}
