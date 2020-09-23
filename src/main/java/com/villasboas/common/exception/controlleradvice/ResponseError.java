package com.villasboas.common.exception.controlleradvice;

import java.util.List;

public class ResponseError {

	private final String errorMessage;
	private final List<String> errorDetails;

	public ResponseError(final String errorMessage, List<String> errorDetails) {
		this.errorMessage = errorMessage;
		errorDetails.sort((s1,s2) -> s1.compareTo(s2));
		this.errorDetails = errorDetails;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public List<String> getErrorDetails() {
		return errorDetails;
	}
	
}
