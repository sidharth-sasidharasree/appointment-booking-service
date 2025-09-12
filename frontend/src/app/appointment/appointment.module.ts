import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AppointmentRoutingModule } from './appointment-routing.module';
import { AppointmentComponent } from './appointment.component';
import { AppointmentListComponent } from './appointment-list/appointment-list.component';
import { AppointmentCreateComponent } from './appointment-create/appointment-create.component';

import { SharedModule } from '../shared/shared.module';


@NgModule({
  declarations: [
    AppointmentComponent,
    AppointmentListComponent,
    AppointmentCreateComponent
  ],
  imports: [
    CommonModule,
    AppointmentRoutingModule,
    SharedModule
  ],
  providers: []
})
export class AppointmentModule { }
