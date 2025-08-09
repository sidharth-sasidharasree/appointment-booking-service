package com.booking.portal.dto;

import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AppointmentRequest {
	
	@NotBlank(message = "Party name is required")
	private String partyName;
	
	@NotNull(message = "Ssn is required")
	private String ssn;
	
	@NotEmpty
	private String reasons;
	
	@NotEmpty
	private String appointmentDates;
	
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getReasons() {
		return reasons;
	}
	public void setReasons(String reasons) {
		this.reasons = reasons;
	}
	public String getAppointmentDates() {
		return appointmentDates;
	}
	public void setAppointmentDates(String appointmentDates) {
		this.appointmentDates = appointmentDates;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(appointmentDates, partyName, reasons, ssn);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppointmentRequest other = (AppointmentRequest) obj;
		return Objects.equals(appointmentDates, other.appointmentDates)
				&& Objects.equals(partyName, other.partyName) && Objects.equals(reasons, other.reasons)
				&& Objects.equals(ssn, other.ssn);
	}
	
	@Override
	public String toString() {
		return "AppointmentRequest [partyName=" + partyName + ", ssn=" + ssn + ", reasons=" + reasons
				+ ", appointmentDates=" + appointmentDates + "]";
	}

}
