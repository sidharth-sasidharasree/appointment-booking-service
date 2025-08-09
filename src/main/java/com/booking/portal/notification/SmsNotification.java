package com.booking.portal.notification;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.booking.portal.model.Appointment;
import com.booking.portal.model.Party;

@Service
@Order(2)
public class SmsNotification implements Notifier {
    private final SmsService smsService;

    public SmsNotification(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void notify(Appointment appointment) {
        Party p = appointment.getParty();
        smsService.sendAppointmentConfirmation(p.getPhone(), p.getName(), appointment.getDate().toString());
    }
}
