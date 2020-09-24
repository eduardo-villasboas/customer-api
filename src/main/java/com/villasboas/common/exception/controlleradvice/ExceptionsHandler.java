package com.villasboas.common.exception.controlleradvice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.villasboas.common.exception.ErrorWhenApplyPatchException;
import com.villasboas.common.exception.NotFoundEntityException;

@ControllerAdvice
public class ExceptionsHandler {

	ExceptionsHandler() {
	}

	@ExceptionHandler(NotFoundEntityException.class)
	ResponseEntity<ResponseError> handleNotFoundEntityException(NotFoundEntityException notFoundException) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ResponseError(notFoundException.getMessage(), Collections.emptyList()));
	}

	@ExceptionHandler(ErrorWhenApplyPatchException.class)
	ResponseEntity<ResponseError> handleErrorWhenApplyPatchException(ErrorWhenApplyPatchException notFoundException) {
		return ResponseEntity.badRequest()
				.body(new ResponseError(notFoundException.getMessage(), Collections.emptyList()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ResponseError> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException methodArgumentNotValidException) {

		final List<String> errorDetails = getInformationErroAsStringList(methodArgumentNotValidException);

		return ResponseEntity.badRequest().body(new ResponseError("Error when validate data.", errorDetails));
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ResponseError> handlerException(Exception exception) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ResponseError("Internal server error.", Collections.emptyList()));
	}

	private List<String> getInformationErroAsStringList(
			MethodArgumentNotValidException methodArgumentNotValidException) {
		return methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
				.map(FieldError::getDefaultMessage).collect(Collectors.toList());
	}
}
