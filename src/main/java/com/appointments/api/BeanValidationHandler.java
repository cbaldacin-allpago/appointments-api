
package com.appointments.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.appointments.api.dtos.ValidationError;


@ControllerAdvice
public class BeanValidationHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationError handleException(final MethodArgumentNotValidException exception) {

		final ValidationError error = new ValidationError(
				"Validation failed. " + exception.getBindingResult().getErrorCount() + " error(s)");
		for (final ObjectError objectError : exception.getBindingResult().getAllErrors()) {
			error.addValidationError(objectError.getDefaultMessage());
		}

		return error;
	}

}
