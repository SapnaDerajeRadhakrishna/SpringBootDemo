package org.maxwell.springboot.controller

import java.io.IOException
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.maxwell.springboot.utils.RestError
import org.maxwell.springboot.utils.RestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import com.fasterxml.jackson.databind.ObjectMapper

@RestController
class SampleController {
    @Autowired
    internal var redisTemplate: RedisTemplate<String, String>? = null
    @Autowired
    internal var mapper: ObjectMapper? = null

    @PostConstruct
    fun init() {
        LOGGER!!.info("dummy:{}", dummy)
    }

    @PostMapping("/updateDetails")
    fun updateStudentDetails(@RequestBody `object`: Object?) {
        LOGGER!!.info("0; recieved request for updating the student details")
        val details1 = mapper!!.convertValue(`object`, StudentDetails::class.java)
        val details2 = mapper!!.convertValue(`object`, TeacherDetails::class.java)
        LOGGER!!.info("0; Student Details: ID: {} Name:{}", details1!!.getStudentID(), details1!!.getStudentName())
        LOGGER!!.info("0; Teacher Details: ID: {} Name:{}", details2!!.getTeacherID(), details2!!.getTeacherName())
        val values = redisTemplate!!.opsForValue()
        values!!.set(String.valueOf(details1!!.getStudentID()), details1!!.getStudentName())
        LOGGER!!.info("wtwzetew {}", values!!.get(String.valueOf(details1!!.getStudentID())))
    }

    @GetMapping("/getDetails")
    fun getStudentDetails(@RequestParam(value = "reset", defaultValue = "false") reset: Boolean) {
        LOGGER!!.info("0; recieved GET request for updating the student details")
        val values = redisTemplate!!.opsForValue()
        LOGGER!!.info("0; data retrieved from redis for {}", values!!.get("123"))
    }

    @ExceptionHandler(Exception::class)
    @Throws(IOException::class)
    fun handleException(ex: Exception?, request: HttpServletRequest?, response: HttpServletResponse?): RestError? {
        return convertToRestError(RestError(HttpStatus.INTERNAL_SERVER_ERROR, ex!!.getMessage()), ex, request,
                response)
    }

    @ExceptionHandler(RestException::class)
    @Throws(IOException::class)
    fun handleException(ex: RestException?, request: HttpServletRequest?, response: HttpServletResponse?): RestError? {
        return convertToRestError(ex!!.getRestError(), ex, request, response)
    }

    private fun convertToRestError(error: RestError?, ex: Exception?, request: HttpServletRequest?,
                                   response: HttpServletResponse?): RestError? {
        LOGGER!!.info("0;processing of " + request!!.getMethod() + " for resource " + request!!.getRequestURI()
                + " failed with error", ex)
        response!!.setStatus(error!!.getHttpStatus())
        return error
    }

    companion object {
        private val dummy: Int = 0
        @Value("\${sample.text.value}")
        fun setSecuredAppsConfigNotificationUrl(something: Int) {
            dummy = something
        }

        private val LOGGER = LoggerFactory.getLogger(SampleController::class.java)
    }
}