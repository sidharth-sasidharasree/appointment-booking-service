/*
 * package com.booking.portal.config;
 * 
 * import org.springframework.boot.CommandLineRunner; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration;
 * 
 * import com.booking.portal.model.Party; import
 * com.booking.portal.repository.PartyRepository;
 * 
 * @Configuration public class DataInitializer {
 * 
 * @Bean public CommandLineRunner loadInitialData(PartyRepository
 * patientRepository) { return args -> { if
 * (patientRepository.findBySsn("123456789").isEmpty()) { Party patient = new
 * Party("Sidharth", "123456789","76231232","sid@gmail.com");
 * patientRepository.save(patient); } }; } }
 */