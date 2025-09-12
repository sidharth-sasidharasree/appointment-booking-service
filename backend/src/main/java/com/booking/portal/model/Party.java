package com.booking.portal.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Party {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)
	private String ssn;

	@OneToMany(mappedBy = "party", fetch = FetchType.LAZY)
	private List<Appointment> appointments;
	
	private String phone;
	
	private String email;

	public Party() {}

	public Party(String name, String ssn, String phone, String email) {
		this.name = name;
		this.ssn = ssn;
		this.phone = phone;
		this.email = email;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Party patient))
			return false;
		return Objects.equals(ssn, patient.ssn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ssn);
	}

	@Override
	public String toString() {
		return "Party [id=" + id + ", name=" + name + ", ssn=" + ssn + ", appointments=" + appointments + ", phone="
				+ phone + ", email=" + email + "]";
	}

}
