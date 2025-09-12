import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Appointment } from '../../shared/models/appointment';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AppointmentService {
  private baseUrl = 'http://localhost:8080/api/appointments';

  constructor(private http: HttpClient) {}

  getAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(this.baseUrl);
  }

  createAppointment(appointment: Appointment): Observable<Appointment> {
    console.log("inside create appointment");
    return this.http.post<Appointment>(this.baseUrl, appointment);
  }
}
