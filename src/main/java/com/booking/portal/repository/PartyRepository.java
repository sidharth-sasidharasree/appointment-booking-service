package com.booking.portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.portal.model.Party;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {

	Optional<Party> findBySsn(String ssn);
	
}
