package com.booking.portal.notification;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendAppointmentConfirmation(String email, String patientName, String appointmentDate) {
        // Simulated email logic (in real-world, use JavaMailSender or a provider like SendGrid)
        System.out.printf("Email sent to %s: Hello %s, your appointment is confirmed for %s%n",
                email, patientName, appointmentDate);
    }
}
