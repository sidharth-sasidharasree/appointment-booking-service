package com.booking.portal.dto;

import java.util.Objects;

public class AppointmentResponse {
	
    private Long appointmentId;
    private String reason;
    private String date;
    private String patientName;
    private String patientSsn;
    

	public AppointmentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppointmentResponse(Long appointmentId, String reason, String date, String patientName, String patientSsn) {
		this.appointmentId = appointmentId;
		this.reason = reason;
		this.date = date;
		this.patientName = patientName;
		this.patientSsn = patientSsn;
	}
    
	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientSsn() {
		return patientSsn;
	}

	public void setPatientSsn(String patientSsn) {
		this.patientSsn = patientSsn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(appointmentId, date, patientName, patientSsn, reason);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppointmentResponse other = (AppointmentResponse) obj;
		return Objects.equals(appointmentId, other.appointmentId) && Objects.equals(date, other.date)
				&& Objects.equals(patientName, other.patientName) && Objects.equals(patientSsn, other.patientSsn)
				&& Objects.equals(reason, other.reason);
	}
	

}
