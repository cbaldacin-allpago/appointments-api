package com.appointments.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.appointments.api.dtos.InputRate;
import com.appointments.api.entities.Appointment;
import com.appointments.api.entities.Patient;
import com.appointments.api.exceptions.PatientNotFoundException;
import com.appointments.api.repositories.PatientRepository;
import com.appointments.api.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientsController {

	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private AppointmentService 	appointmentService;

	/**
	 * 
	 * as a doctor I want to create my patients
	 * 
	 * @param patient
	 * @return patient created
	 */

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Patient create(@RequestBody @Valid final Patient customer) {
		return patientRepository.save(customer);
	}
	
	/**
	 * as a patient I want to see my next appointment
	 * 
	 * @param id
	 * @return appointment
	 * @throws PatientNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/{id}/next-appointment")
	@ResponseStatus(HttpStatus.OK)
	public Appointment findNextAppointmentByCustomer(@PathVariable final String id) throws PatientNotFoundException {

		return appointmentService.getNextAppointmentByPatient(id);

	}

	/**
	 * as a patient I want to rate my last appointment.
	 * 
	 * Assumption 1: last appointment is based on last start date, therefore it
	 * can be an appointment still under attendance
	 * 
	 * Assumption 2: In order to provide flexibility, the rate is for the
	 * appointmentId indicated. If the value is null then by default the last
	 * appointment is rated.
	 * 
	 * @param id
	 * @param rate
	 * @return appointment
	 * @throws PatientNotFoundException 
	 */
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}/rate")
	@ResponseStatus(HttpStatus.OK)
	public Appointment rate(@RequestBody @Valid final InputRate inputRate, @PathVariable final String id) throws PatientNotFoundException {

		return appointmentService.rateAppointment(id, inputRate);

	}

}
