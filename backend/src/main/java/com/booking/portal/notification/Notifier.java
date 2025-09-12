package com.booking.portal.notification;

import com.booking.portal.model.Appointment;

public interface Notifier {
	
    void notify(Appointment appointment);
    
}
