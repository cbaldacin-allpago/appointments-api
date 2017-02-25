package com.appointments.api.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ValidationError  {

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private final List<String> errors = new ArrayList<>();

	private final String errorMessage;

	/**
	 * The Constructor.
	 * 
	 * @param errorMessage
	 *            a new error message.
	 */
	public ValidationError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Adds a new validation error.
	 * 
	 * @param error
	 */
	public void addValidationError(final String error) {
		errors.add(error);
	}

	/**
	 * @return a list of errors.
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * Gets the error message.
	 * 
	 * @return error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

}
