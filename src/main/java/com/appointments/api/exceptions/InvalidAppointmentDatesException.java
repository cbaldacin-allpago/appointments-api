package com.appointments.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Appoitnment dates are invalid, either they are in the past or end is before start")
public class InvalidAppointmentDatesException extends Exception {

	private static final long serialVersionUID = 8224405336278490580L;

}
