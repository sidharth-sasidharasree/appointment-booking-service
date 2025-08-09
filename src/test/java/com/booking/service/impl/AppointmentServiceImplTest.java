package com.booking.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

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
import com.booking.portal.service.impl.AppointmentServiceImpl;

import jakarta.persistence.EntityNotFoundException;

class AppointmentServiceImplTest {

    @Mock
    private PartyRepository partyRepo;

    @Mock
    private AppointmentRepository appointmentRepo;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- createAppointments ----------
    @Test
    void createAppointments_existingParty_success() {
        AppointmentRequest request = new AppointmentRequest();
        request.setSsn("123");
        request.setPartyName("John Doe");
        request.setReasons("Checkup");
        request.setAppointmentDates(LocalDateTime.now().toString());

        Party party = new Party("John Doe", "123", null, null);
        Appointment savedAppointment = new Appointment("Checkup", LocalDateTime.now().toString(), party);
        AppointmentResponse expectedResponse = new AppointmentResponse();

        when(partyRepo.findBySsn("123")).thenReturn(Optional.of(party));
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(savedAppointment);
        when(appointmentMapper.toResponse(savedAppointment)).thenReturn(expectedResponse);

        AppointmentResponse response = appointmentService.createAppointments(request);

        assertEquals(expectedResponse, response);
        verify(eventPublisher).publishEvent(any(AppointmentConfirmedEvent.class));
        verify(appointmentRepo).save(any(Appointment.class));
    }

    @Test
    void createAppointments_newParty_success() {
        AppointmentRequest request = new AppointmentRequest();
        request.setSsn("456");
        request.setPartyName("Jane Doe");
        request.setReasons("Consultation");
        request.setAppointmentDates(LocalDateTime.now().toString());

        Party savedParty = new Party("Jane Doe", "456", null, null);
        
        // simulate party not found
        when(partyRepo.findBySsn("456")).thenReturn(Optional.empty());
        Appointment savedAppointment = new Appointment("Consultation", LocalDateTime.now().toString(), savedParty);
        AppointmentResponse expectedResponse = new AppointmentResponse();

        when(partyRepo.save(any(Party.class))).thenReturn(savedParty);
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(savedAppointment);
        when(appointmentMapper.toResponse(savedAppointment)).thenReturn(expectedResponse);

        AppointmentResponse response = appointmentService.createAppointments(request);

        assertEquals(expectedResponse, response);
        verify(partyRepo).save(any(Party.class));
        verify(eventPublisher).publishEvent(any(AppointmentConfirmedEvent.class));
    }

    @Test
    void createAppointments_failure_throwsApiException() {
        AppointmentRequest request = new AppointmentRequest();
        request.setSsn("789");
        request.setPartyName("Error Case");

        when(partyRepo.findBySsn("789")).thenThrow(new RuntimeException("DB error"));

        ApiException ex = assertThrows(ApiException.class, () -> appointmentService.createAppointments(request));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    // ---------- getAppointmentsByReason ----------
    @Test
    void getAppointmentsByReason_success() {
        String reason = "Checkup";
        List<Appointment> appointments = Collections.singletonList(new Appointment());
        List<AppointmentResponse> responses = Collections.singletonList(new AppointmentResponse());

        when(appointmentRepo.findByReasonIgnoreCase(reason)).thenReturn(appointments);
        when(appointmentMapper.toResponseList(appointments)).thenReturn(responses);

        List<AppointmentResponse> result = appointmentService.getAppointmentsByReason(reason);

        assertEquals(responses, result);
        verify(appointmentRepo).findByReasonIgnoreCase(reason);
    }

    @Test
    void getAppointmentsByReason_failure() {
        when(appointmentRepo.findByReasonIgnoreCase(any())).thenThrow(new RuntimeException("Error"));

        ApiException ex = assertThrows(ApiException.class, () -> appointmentService.getAppointmentsByReason("anything"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    // ---------- deleteAppointmentsBySSN ----------
    @Test
    void deleteAppointmentsBySSN_success() {
        Party party = new Party();
        when(partyRepo.findBySsn("123")).thenReturn(Optional.of(party));

        appointmentService.deleteAppointmentsBySSN("123");

        verify(appointmentRepo).deleteByPartySsn("123");
    }

    @Test
    void deleteAppointmentsBySSN_partyNotFound() {
        when(partyRepo.findBySsn("404")).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> appointmentService.deleteAppointmentsBySSN("404"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void deleteAppointmentsBySSN_deleteFailure() {
        Party party = new Party();
        when(partyRepo.findBySsn("123")).thenReturn(Optional.of(party));
        doThrow(new RuntimeException("DB error")).when(appointmentRepo).deleteByPartySsn("123");

        ApiException ex = assertThrows(ApiException.class, () -> appointmentService.deleteAppointmentsBySSN("123"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }

    // ---------- findLatestAppointmentBySSN ----------
    @Test
    void findLatestAppointmentBySSN_success() {
        Party party = new Party();
        party.setAppointments(Collections.singletonList(new Appointment()));

        Appointment latest = new Appointment();
        AppointmentResponse expectedResponse = new AppointmentResponse();

        when(partyRepo.findBySsn("123")).thenReturn(Optional.of(party));
        when(appointmentRepo.findTopBySsnOrderByDateDesc(eq("123"), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(latest));
        when(appointmentMapper.toResponse(latest)).thenReturn(expectedResponse);

        AppointmentResponse result = appointmentService.findLatestAppointmentBySSN("123");

        assertEquals(expectedResponse, result);
    }

    @Test
    void findLatestAppointmentBySSN_failure() {
        when(partyRepo.findBySsn("123")).thenThrow(new RuntimeException("Error"));

        ApiException ex = assertThrows(ApiException.class, () -> appointmentService.findLatestAppointmentBySSN("123"));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}
