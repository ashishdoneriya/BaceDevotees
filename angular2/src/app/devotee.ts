export class Devotee {
	id?: string;
	name: string;
	dob?: String;
	fatherName?: string;
	permanentAddress?: string;
	currentAddress?: string;
	mobileNumber?: string;
	email?: string;
	emergencyNumber?: string;
	baceJoinDate?: String;
	baceLeftDate?: String;

	constructor() {
		this.name = '';
		this.fatherName = '';
		this.permanentAddress = '';
		this.currentAddress = '';
		this.mobileNumber = '';
		this.email = '';
		this.emergencyNumber = '';
		this.dob = '';
		this.baceJoinDate = '';
		this.baceLeftDate = '';
	}
}
