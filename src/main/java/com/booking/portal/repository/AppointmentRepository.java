package com.booking.portal.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.booking.portal.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	
	@Query("SELECT a FROM Appointment a WHERE LOWER(a.reason) = LOWER(:reason)")
	List<Appointment> findByReasonIgnoreCase(@Param("reason") String reason);
	
	@Query("SELECT a FROM Appointment a WHERE a.party.ssn = :ssn ORDER BY a.date DESC")
	List<Appointment> findTopBySsnOrderByDateDesc(@Param("ssn") String ssn, Pageable pageable);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Appointment a WHERE a.party.ssn = :ssn")
	void deleteByPartySsn(@Param("ssn") String ssn);
	
}
