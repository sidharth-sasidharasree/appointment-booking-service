package com.booking.portal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.portal.dto.AppointmentRequest;
import com.booking.portal.dto.AppointmentResponse;
import com.booking.portal.service.AppointmentService;

@RestController
@RequestMapping("/api")
public class AppointmentController {

	private final AppointmentService hospitalService;
	
	public AppointmentController(AppointmentService hospitalService) {
		this.hospitalService = hospitalService;
	}

	@PostMapping("/appointments")
	public ResponseEntity<AppointmentResponse> createAppointments(@RequestBody AppointmentRequest request) {
		AppointmentResponse created = hospitalService.createAppointments(request);
		return new ResponseEntity<>(created, HttpStatus.OK);
	}

	@GetMapping("/appointments")
	public ResponseEntity<List<AppointmentResponse>> getAppointmentsByReason(@RequestParam String keyword) {
		List<AppointmentResponse> found = hospitalService.getAppointmentsByReason(keyword);
		return new ResponseEntity<>(found, HttpStatus.OK);
	}

	@DeleteMapping("/appointments")
	public ResponseEntity<String> deleteAppointmentsBySSN(@RequestParam String ssn) {
		hospitalService.deleteAppointmentsBySSN(ssn);
		return new ResponseEntity<>("Deleted all appointments for SSN: " + ssn, HttpStatus.OK);
	}

	@GetMapping("/appointments/latest")
	public ResponseEntity<AppointmentResponse> getLatestAppointment(@RequestParam String ssn) {
		AppointmentResponse latest = hospitalService.findLatestAppointmentBySSN(ssn);
		return new ResponseEntity<>(latest, HttpStatus.OK);
	}
}
