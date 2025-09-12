package com.booking.portal.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.booking.portal.notification.CompositeNotifier;

@Component
public class AppointmentConfirmedListener {

    private final CompositeNotifier compositeNotifier;

    public AppointmentConfirmedListener(CompositeNotifier compositeNotifier) {
        this.compositeNotifier = compositeNotifier;
    }

    @EventListener
    public void handleAppointmentConfirmed(AppointmentConfirmedEvent event) {
    	compositeNotifier.notify(event.getAppointment());
    }
}
