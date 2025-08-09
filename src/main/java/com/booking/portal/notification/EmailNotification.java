package com.booking.portal.notification;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.booking.portal.model.Appointment;
import com.booking.portal.model.Party;

@Service
@Order(1)
public class EmailNotification implements Notifier {
    private final EmailService emailService;

    public EmailNotification(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notify(Appointment appointment) {
        Party p = appointment.getParty();
        emailService.sendAppointmentConfirmation(p.getEmail(), p.getName(), appointment.getDate().toString());
    }
}
