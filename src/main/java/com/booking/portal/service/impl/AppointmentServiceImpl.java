package com.booking.portal.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booking.portal.annotation.LogExecutionTime;
import com.booking.portal.dto.AppointmentRequest;
import com.booking.portal.dto.AppointmentResponse;
import com.booking.portal.event.AppointmentConfirmedEvent;
import com.booking.portal.exception.ApiException;
import com.booking.portal.mapper.AppointmentMapper;
import com.booking.portal.model.Appointment;
import com.booking.portal.model.Party;
import com.booking.portal.repository.AppointmentRepository;
import com.booking.portal.repository.PartyRepository;
import com.booking.portal.service.AppointmentService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	
	private final ApplicationEventPublisher eventPublisher;

	private final PartyRepository partyRepo;

	private final AppointmentRepository appointmentRepo;
	
	private final AppointmentMapper appointmentMapper;

	private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	public AppointmentServiceImpl(PartyRepository partyRepo, AppointmentRepository appointmentRepo, AppointmentMapper appointmentMapper, ApplicationEventPublisher eventPublisher) {
		this.partyRepo = partyRepo;
		this.appointmentRepo = appointmentRepo;
		this.appointmentMapper = appointmentMapper;
		this.eventPublisher = eventPublisher;
	}

    /**
     * Creates multiple appointments in bulk based on the request.
     * Evicts caches related to appointments by SSN to keep data consistent.
     */
	@Override
	@LogExecutionTime
	@CacheEvict(value = { "appointmentsBySsn", "ssn" }, allEntries = true)
	public AppointmentResponse createAppointments(AppointmentRequest request) {
		logger.info("Inside bulkCreateAppointments {}", request.toString());
		try {
			Party party = findOrCreateParty(request.getSsn(), request.getPartyName());
			Appointment appointment = new Appointment(request.getReasons(), request.getAppointmentDates(), party);
			Appointment savedAppointments = appointmentRepo.save(appointment);
	        eventPublisher.publishEvent(new AppointmentConfirmedEvent(this, appointment));
			return appointmentMapper.toResponse(savedAppointments);
		} catch (Exception e) {
			logger.error("Error during createAppointments for SSN {}: {}", request.getSsn(), e.getMessage(), e);
			throw new ApiException("Failed to create appointments", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    /**
     * Finds a party by SSN or creates a new one if not found.
     */
	private Party findOrCreateParty(String ssn, String name) {
	    try {
	        Party existing = findPartyBySSN(ssn);
	        logger.info("Existing party found, SSN: {}", ssn);
	        return existing;
	    } catch (EntityNotFoundException e) {
	        logger.info("Creating new party with SSN: {}", ssn);
	        Party newParty = new Party(name, ssn, null, null);
	        return saveParty(newParty);
	    }
	}

    /**
     * Attempts to find a party by SSN.
     * Throws EntityNotFoundException if not found.
     */
	private Party findPartyBySSN(String ssn) {
		return partyRepo.findBySsn(ssn).orElseThrow(() -> {
			logger.warn("Party not found with SSN: {}", ssn);
			return new EntityNotFoundException("Party not found with SSN: " + ssn);
		});
	}

    /**
     * Saves the party entity in a transactional context.
     */
	@Transactional
	private Party saveParty(Party party) {
		return partyRepo.save(party);
	}

    /**
     * Retrieves appointments filtered by a given reason keyword.
     */
	@Override
	@LogExecutionTime
	public List<AppointmentResponse> getAppointmentsByReason(String reasonKeyword) {
		logger.info("Inside getAppointmentsByReason {}", reasonKeyword);
		try {
			List<Appointment> appointments = appointmentRepo.findByReasonIgnoreCase(reasonKeyword);
			return appointmentMapper.toResponseList(appointments);
		} catch (Exception e) {
			logger.error("Failed to get appointments by reason '{}': {}", reasonKeyword, e.getMessage(), e);
			throw new ApiException("Failed to retrieve task with reason: " + reasonKeyword, HttpStatus.NOT_FOUND);
		}
	}

    /**
     * Deletes all appointments for a party identified by SSN.
     * Throws 404 if party does not exist.
     * Evicts caches related to appointments to maintain cache consistency.
     */
	@Override
	@LogExecutionTime
	@CacheEvict(value = { "appointmentsBySsn", "ssn" }, allEntries = true)
	public void deleteAppointmentsBySSN(String ssn) {
		logger.info("Inside deleteAppointmentsBySSN {}", ssn);
		Optional<Party> optionalParty = partyRepo.findBySsn(ssn);
		if (optionalParty.isEmpty()) {
			logger.warn("Party not found with SSN: {}", ssn);
			throw new ApiException("Party not found with SSN: " + ssn, HttpStatus.NOT_FOUND);
		}
		try {
	        appointmentRepo.deleteByPartySsn(ssn);
	        logger.info("Deleted appointments for SSN: {}", ssn);
		} catch (Exception e) {
			logger.error("Failed to delete appointments for SSN: {}. Error: {}", ssn, e.getMessage(), e);
			throw new ApiException("Failed to delete appointments for SSN: " + ssn, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    /**
     * Finds the latest appointment for a party by SSN.
     * Results are cached for faster subsequent retrievals.
     */
	@Override
	@LogExecutionTime
	@Cacheable(value = "appointmentsBySsn", key = "#ssn")
	public AppointmentResponse findLatestAppointmentBySSN(String ssn) {
		logger.info("Inside findLatestAppointmentBySSN {}", ssn);
		try {
			Party party = findPartyBySSN(ssn);
			if (party == null || party.getAppointments() == null) {
				return null;
			}
			Appointment latest = appointmentRepo.findTopBySsnOrderByDateDesc(ssn, PageRequest.of(0, 1)).get(0);
			return appointmentMapper.toResponse(latest);
		} catch (Exception e) {
			logger.error("Error finding latest appointment for SSN {}: {}", ssn, e.getMessage(), e);
			throw new ApiException("Failed to retrieve task with ssn: " + ssn, HttpStatus.NOT_FOUND);
		}
	}
}
