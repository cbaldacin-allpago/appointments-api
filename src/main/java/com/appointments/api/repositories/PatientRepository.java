package com.appointments.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.appointments.api.entities.Patient;

public interface PatientRepository extends MongoRepository<Patient, String> {

}
