package com.villasboas.common.exception.controlleradvice;

public class ResponseError {

	private final String errorMessage;

	public ResponseError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
}
