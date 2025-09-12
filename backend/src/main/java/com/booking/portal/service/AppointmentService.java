package com.booking.portal.service;

import java.util.List;

import com.booking.portal.dto.AppointmentRequest;
import com.booking.portal.dto.AppointmentResponse;

public interface AppointmentService {
	
	AppointmentResponse createAppointments(AppointmentRequest request);
	
	List<AppointmentResponse> getAppointmentsByReason(String reasonKeyword);
	
	void deleteAppointmentsBySSN(String ssn);
	
	AppointmentResponse findLatestAppointmentBySSN(String ssn);
	

}
