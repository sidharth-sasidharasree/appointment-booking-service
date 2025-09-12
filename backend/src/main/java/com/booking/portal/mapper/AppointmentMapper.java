package com.booking.portal.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.booking.portal.dto.AppointmentResponse;
import com.booking.portal.model.Appointment;

@Component
public class AppointmentMapper {
	
    public List<AppointmentResponse> toResponseList(List<Appointment> appointments) {
        return appointments.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
            appointment.getId(),
            appointment.getReason(),
            appointment.getDate(),
            appointment.getParty().getName(),
            appointment.getParty().getSsn()
        );
    }

}
