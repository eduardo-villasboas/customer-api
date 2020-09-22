package com.villasboas.common.exception.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(notFoundException.getMessage()));
	}

	@ExceptionHandler(ErrorWhenApplyPatchException.class)
	ResponseEntity<ResponseError> handleErrorWhenApplyPatchException(ErrorWhenApplyPatchException notFoundException) {
		return ResponseEntity.badRequest().body(new ResponseError(notFoundException.getMessage()));
	}

}
