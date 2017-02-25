package com.appointments.api;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.appointments.api.entities.Appointment;
import com.appointments.api.exceptions.InvalidAppointmentDatesException;
import com.appointments.api.exceptions.PatientNotFoundException;
import com.appointments.api.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	/**
	 * as a doctor I want to create appointments for a patient
	 * 
	 * @param appointment
	 * @return appointment created
	 * @throws PatientNotFoundException
	 * @throws InvalidAppointmentDatesException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Appointment create(@RequestBody @Valid final Appointment appointment)
			throws PatientNotFoundException, InvalidAppointmentDatesException {
		return appointmentService.createAppointment(appointment);
	}

	/**
	 * as a doctor I want to see an overview of all appointments and their
	 * ratings
	 * 
	 * @return List of Appointments
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<Appointment> findAppointments() {
		return appointmentService.getAllAppointmens();
	}

	/**
	 * as a doctor I want to see an overview of the next week’s appointments
	 * 
	 * Assuming next week is from next Monday to subsequent Sunday.
	 * 
	 * @return List of appointments
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/next-week")
	@ResponseStatus(HttpStatus.OK)
	public List<Appointment> findNextWeekAppointments() {

		LocalDateTime todayAtMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
		LocalDateTime nextMonday = todayAtMidnight.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
		LocalDateTime sunday = LocalDateTime.of(nextMonday.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toLocalDate(),
				LocalTime.MAX);

		return appointmentService.getAppointmentsBetween(nextMonday, sunday);
	}

}
