package com.appointments.api.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The Patient informed has not been found")
public class PatientNotFoundException extends Exception {

	private static final long serialVersionUID = -6784672501286896217L;

}
