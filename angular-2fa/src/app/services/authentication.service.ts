import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {RegisterRequest} from "../models/register-request";
import {AuthenticationResponse} from "../models/authentication-response";
import {VerificationRequest} from "../models/verification-request";
import {AuthenticationRequest} from "../models/authentication-request";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  // Base url of our backend
  private baseUrl = 'http://localhost:8080/api/v1/auth'

  // Constructor, to be able to use http client service
  constructor(
    private http: HttpClient
  ) { }

  // Register method
  register(registerRequest: RegisterRequest) {
    return this.http.post<AuthenticationResponse>(`${this.baseUrl}/register`, registerRequest);
  }

  // Login method
  login(authRequest: AuthenticationRequest) {
    return this.http.post<AuthenticationResponse>(`${this.baseUrl}/authenticate`, authRequest);
  }

  // Verify method
  verifyCode(verificationRequest: VerificationRequest) {
    return this.http.post<AuthenticationResponse>(`${this.baseUrl}/verify`, verificationRequest);
  }

}
