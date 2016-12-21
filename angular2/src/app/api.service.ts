import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

import { Devotee } from './devotee';

@Injectable()
export class ApiService {

	constructor(private http: Http) { }

	login(password: string) {
		return this.http.post('/dologin', { 'password': password }).map(this.extractData);
	}
	logout() {
		this.http.post('/logout', {});
	}

	list(searchQuery: string, page?: any, maximum?: any): Observable<Devotee[]> {
		if (searchQuery || page || maximum) {
			let params: URLSearchParams = new URLSearchParams();
			if (searchQuery) {
				params.set("searchQuery", searchQuery);
			}
			if (page) {
				params.set('page', page);
			}
			if (maximum) {
				params.set('maximum', maximum);
			}
			return this.http.get('/devotees', { search: params })
				.map(this.extractData)
				.catch(this.handleError);
		} else {
			return this.http.get('/apis/devotees')
				.map(this.extractData)
				.catch(this.handleError);
		}
	}

	get(id: any): Observable<Devotee> {
		return this.http.get('/apis/devotees/' + id)
						.map(this.extractData)
						.catch(this.handleError);
	}

	add(devotee: Devotee) {
		let headers = new Headers({ 'Content-Type': 'application/json' });
		let options = new RequestOptions({ headers: headers });
		return this.http.post('/apis/add', Devotee, options)
						.map(this.extractData)
        				.catch(this.handleError);
	}

	update(devotee: Devotee) {
		let headers = new Headers({ 'Content-Type': 'application/json' });
		let options = new RequestOptions({ headers: headers });
		return this.http.post('/apis/update', Devotee, options)
						.map(this.extractData)
        				.catch(this.handleError);
	}

	delete(id: any) {
		let headers = new Headers({ 'Content-Type': 'application/json' });
		let options = new RequestOptions({ headers: headers });
		return this.http.post('/apis/delete/' + id, options)
						.map(this.extractData)
        				.catch(this.handleError);
	}

	private extractData(res: Response) {
		let body = res.json();
		return body.data || null;
	}

	private handleError(error: Response | any) {
		// In a real world app, we might use a remote logging infrastructure
		let errMsg: string;
		if (error instanceof Response) {
			const body = error.json() || '';
			const err = body.error || JSON.stringify(body);
			errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
		} else {
			errMsg = error.message ? error.message : error.toString();
		}
		console.error(errMsg);
		return Observable.throw(errMsg);
	}
}
