package com.booking.portal.notification;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendAppointmentConfirmation(String phoneNumber, String patientName, String appointmentDate) {
        // Simulated SMS logic (in real-world, use a provider like Twilio, AWS SNS, etc.)
        System.out.printf("SMS sent to %s: Hello %s, your appointment is confirmed for %s%n",
                phoneNumber, patientName, appointmentDate);
    }
}
