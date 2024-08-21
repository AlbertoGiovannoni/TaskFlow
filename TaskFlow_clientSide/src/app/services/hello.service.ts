import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HelloService {
  private apiUrl = 'http://localhost:3000/api/hello';

  constructor(private http: HttpClient) { }

  sayHello(message: string): Observable<any> {
    return this.http.post<any>(this.apiUrl, { message });
  }
}