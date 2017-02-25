package com.appointments.api.test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.appointments.api.entities.Appointment;
import com.appointments.api.entities.Patient;
import com.appointments.api.repositories.AppointmentRepository;
import com.appointments.api.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractTest {

	protected static final String APPOINTMENT_URL = "/api/v1/appointments/";
	protected static final String PATIENTS_URL = "/api/v1/patients/";

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper mapper;

	@Autowired
	protected PatientRepository patientRepository;

	@Autowired
	protected AppointmentRepository appointmentRepository;

	protected List<Appointment> createAppointments() {

		List<Appointment> result = new ArrayList<Appointment>();

		LocalDateTime from = LocalDateTime.now().minusDays(7);
		LocalDateTime to = LocalDateTime.now().plusDays(14);
		LocalDateTime counterDate = from;

		while (counterDate.isBefore(to)) {

			if (!counterDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
					&& !counterDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {

				LocalDateTime start = LocalDateTime.of(counterDate.toLocalDate(), LocalTime.of(13, 0));

				result.add(createAppointment("Name-" + counterDate.getDayOfMonth(), start, start.plusHours(1)));
			}
			counterDate = counterDate.plusDays(1);

		}

		return appointmentRepository.save(result);
	}

	protected List<Appointment> createAppointmentsForPatient(final String patientId) {

		List<Appointment> result = new ArrayList<Appointment>();

		LocalDateTime from = LocalDateTime.now().minusDays(7);
		LocalDateTime to = LocalDateTime.now().plusDays(14);
		LocalDateTime counterDate = from;

		while (counterDate.isBefore(to)) {

			if (!counterDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)
					&& !counterDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {

				LocalDateTime start = LocalDateTime.of(counterDate.toLocalDate(), LocalTime.of(13, 0));

				result.add(createAppointmentForPatientId(patientId, start, start.plusHours(1)));
			}
			counterDate = counterDate.plusDays(1);

		}

		return appointmentRepository.save(result);
	}

	protected Appointment createAppointment(String patientName, LocalDateTime start, LocalDateTime end) {
		Appointment appointment = new Appointment();

		appointment.setPatientId(createPatient(patientName, "Default"));
		appointment.setStart(start);
		appointment.setEnd(end);

		return appointment;
	}

	protected String createPatient(final String name, final String surname) {

		Patient patient = new Patient(name, surname);
		return patientRepository.save(patient).getId();
	}

	protected Appointment createAppointmentForPatientId(String patientId, LocalDateTime start, LocalDateTime end) {
		Appointment appointment = new Appointment();
		appointment.setPatientId(patientId);
		appointment.setStart(start);
		appointment.setEnd(end);

		return appointment;
	}

	protected Appointment createAppointmentInvalidPatient() {
		Appointment appointment = new Appointment();

		appointment.setPatientId("invalid-id");
		appointment.setStart(LocalDateTime.now().plusHours(2));
		appointment.setEnd(LocalDateTime.now().plusHours(3));

		return appointment;
	}

}
