package com.appointments.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.appointments.api.dtos.InputRate;
import com.appointments.api.entities.Appointment;
import com.appointments.api.exceptions.InvalidAppointmentDatesException;
import com.appointments.api.exceptions.PatientNotFoundException;
import com.appointments.api.repositories.AppointmentRepository;
import com.appointments.api.repositories.PatientRepository;

@Service
public class AppointmentService {

	private static final Sort APPOINTMENT_START_DESC = new Sort(Direction.DESC, "start");

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private PatientRepository patientRepository;

	public Appointment createAppointment(final Appointment appointment)
			throws PatientNotFoundException, InvalidAppointmentDatesException {

		validateAppointment(appointment);

		return appointmentRepository.save(appointment);

	}

	private void validateAppointment(Appointment appointment)
			throws PatientNotFoundException, InvalidAppointmentDatesException {

		if (!isPatientFound(appointment.getPatientId())) {
			throw new PatientNotFoundException();
		}

		validateAppointmentDates(appointment);

	}

	private void validateAppointmentDates(Appointment appointment) throws InvalidAppointmentDatesException {

		LocalDateTime now = LocalDateTime.now();

		if (appointment.getStart().isBefore(now) || appointment.getEnd().isBefore(now)
				|| appointment.getEnd().isBefore(appointment.getStart())) {
			throw new InvalidAppointmentDatesException();
		}

	}

	public List<Appointment> getAllAppointmens() {

		return appointmentRepository.findAll(APPOINTMENT_START_DESC);
	}

	public List<Appointment> getAppointmentsBetween(final LocalDateTime start, final LocalDateTime end) {
		return appointmentRepository.findByStartBetween(start, end);
	}

	public Appointment getNextAppointmentByPatient(final String patientId) throws PatientNotFoundException {

		if (isPatientFound(patientId)) {
			LocalDateTime now = LocalDateTime.now();
			Appointment appointment = appointmentRepository
					.findFirstByPatientIdAndStartAfterOrderByStartAsc(patientId, now);
			return appointment;
		} else {
			throw new PatientNotFoundException();
		}

	}

	public Appointment rateAppointment(final String patientId, final InputRate inputRate) throws PatientNotFoundException {
		
		// This update action is not atomic, however since the rating is always
		// for one patient, possible race conditions wouldn't cause major
		// issues.
		// For simplicity I'm keeping like this, otherwise I would have to write
		// a custom update implementation.
		
		if (isPatientFound(patientId)) {

			Appointment appointment = null;
	
			if (inputRate.getAppointmentId() == null) {
				// get the last
				appointment = appointmentRepository.findFirstByPatientIdAndStartBeforeOrderByStartDesc(
						patientId, LocalDateTime.now());
			} else {
				appointment = appointmentRepository.findOne(inputRate.getAppointmentId());
			}
			appointment.setRate(inputRate.getRate());
	
			return appointmentRepository.save(appointment);
		
		} else {
			throw new PatientNotFoundException();
		}
	}

	private boolean isPatientFound(final String id) {
		return patientRepository.exists(id);
	}

}
