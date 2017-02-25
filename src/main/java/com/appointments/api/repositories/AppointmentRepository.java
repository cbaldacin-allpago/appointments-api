package com.appointments.api.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.appointments.api.entities.Appointment;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

	List<Appointment> findByStartBetween(LocalDateTime from, LocalDateTime to);

	Appointment findFirstByPatientIdAndStartAfterOrderByStartAsc(String id, LocalDateTime now);

	Appointment findFirstByPatientIdAndStartBeforeOrderByStartDesc(String id, LocalDateTime now);

}
