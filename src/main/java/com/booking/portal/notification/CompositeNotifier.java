package com.booking.portal.notification;

import java.util.List;

import org.springframework.stereotype.Component;

import com.booking.portal.model.Appointment;

@Component
public class CompositeNotifier implements Notifier {

    private final List<Notifier> notifiers;

    public CompositeNotifier(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }

    @Override
    public void notify(Appointment appointment) {
        notifiers.forEach(n -> n.notify(appointment));
    }
}
