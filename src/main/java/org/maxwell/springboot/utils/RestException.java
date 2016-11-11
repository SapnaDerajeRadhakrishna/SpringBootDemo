package org.maxwell.springboot.utils;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {

	private static final long serialVersionUID = 6160934062358225676L;
	private RestError restError;

	public RestException(HttpStatus httpStatus, String message) {
		super(message);
		this.restError = new RestError(httpStatus, message);
	}

	public RestError getRestError() {
		return restError;
	}
}
