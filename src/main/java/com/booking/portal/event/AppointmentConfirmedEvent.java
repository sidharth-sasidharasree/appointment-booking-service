package com.booking.portal.event;

import org.springframework.context.ApplicationEvent;

import com.booking.portal.model.Appointment;

public class AppointmentConfirmedEvent extends ApplicationEvent {
	
    private final Appointment appointment;

    public AppointmentConfirmedEvent(Object source, Appointment appointment) {
        super(source);
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }
    
}
