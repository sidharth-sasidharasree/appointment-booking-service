import { Component } from '@angular/core';
import { Appointment } from '../../shared/models/appointment';
import { AppointmentService } from '../services/appointment.service'

@Component({
  selector: 'app-appointment-create',
  standalone: false,
  templateUrl: './appointment-create.component.html',
  styleUrl: './appointment-create.component.scss'
})
export class AppointmentCreateComponent {
    appointment: Appointment = {
      partyName: '',
      ssn: '',
      phone: '',
      email: '',
      reasons: '',
      appointmentDates: ''
    };

    constructor(private service: AppointmentService) {}

    save() {
      console.log("save");
      this.service.createAppointment(this.appointment).subscribe({
        next: (response) => console.log('Success', response),
        error: (err) => console.error('Error', err),
       });
    }

}
