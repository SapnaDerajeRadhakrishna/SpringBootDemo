package org.maxwell.springboot.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.maxwell.springboot.utils.RestError;
import org.maxwell.springboot.utils.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class SampleController {

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

	@PostMapping("/updateDetails")
	public void updateStudentDetails(@RequestBody Object object) {
		LOGGER.info("0; recieved request for updating the student details");
		ObjectMapper mapper = new ObjectMapper();
		StudentDetails details1 = mapper.convertValue(object, StudentDetails.class);
		TeacherDetails details2 = mapper.convertValue(object, TeacherDetails.class);
		LOGGER.info("0; Student Details: ID: {} Name:{}", details1.getStudentID(), details1.getStudentName());
		LOGGER.info("0; Teacher Details: ID: {} Name:{}", details2.getTeacherID(), details2.getTeacherName());
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(String.valueOf(details1.getStudentID()), details1.getStudentName());

	}

	@GetMapping("/getDetails")
	public void getStudentDetails(@RequestParam(value = "reset", defaultValue = "false") boolean reset) {
		LOGGER.info("0; recieved GET request for updating the student details");
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		LOGGER.info("0; data retrieved from redis for {}", values.get("123"));
	}

	@ExceptionHandler(Exception.class)
	public RestError handleException(Exception ex, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		return convertToRestError(new RestError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()), ex, request,
				response);
	}

	@ExceptionHandler(RestException.class)
	public RestError handleException(RestException ex, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		return convertToRestError(ex.getRestError(), ex, request, response);
	}

	private RestError convertToRestError(RestError error, Exception ex, HttpServletRequest request,
			HttpServletResponse response) {
		LOGGER.info("0;processing of " + request.getMethod() + " for resource " + request.getRequestURI()
				+ " failed with error", ex);
		response.setStatus(error.getHttpStatus());
		return error;
	}
}
