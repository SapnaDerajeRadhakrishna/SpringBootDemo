package org.maxwell.springboot.utils;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class RestError implements Serializable {

	private static final long serialVersionUID = -1999622410010562693L;
	private HttpStatus httpStatus;
	private String message;

	public RestError(HttpStatus httpStatus, String message) {
		super();
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public int getHttpStatus() {
		return httpStatus.value();
	}

	public String getMessage() {
		return message;
	}
}
