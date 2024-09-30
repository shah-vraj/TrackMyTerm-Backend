package com.trackmyterm.util.controlleradvice

import com.trackmyterm.exception.InvalidPasswordException
import com.trackmyterm.exception.OtpVerificationException
import com.trackmyterm.exception.UserAlreadyRegisteredException
import com.trackmyterm.exception.UserNotFoundException
import com.trackmyterm.response.ErrorResponseBody
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object AuthControllerAdvice {

    private val logger = LoggerFactory.getLogger(AuthControllerAdvice::class.java)

    fun handleUserNotFoundException(exception: UserNotFoundException): ResponseEntity<ErrorResponseBody> {
        logger.error("User not found: ${exception.message}")
        val response = ErrorResponseBody.error(exception.message.orEmpty())
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    fun handleUserAlreadyRegisteredException(exception: UserAlreadyRegisteredException): ResponseEntity<ErrorResponseBody> {
        logger.error("User already registered: ${exception.message}")
        val response = ErrorResponseBody.error(exception.message.orEmpty())
        return ResponseEntity(response, HttpStatus.CONFLICT)
    }

    fun handleInvalidPasswordException(exception: InvalidPasswordException): ResponseEntity<ErrorResponseBody> {
        logger.error("User password does not match: ${exception.message}")
        val response = ErrorResponseBody.error(exception.message.orEmpty())
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    fun handleOtpVerificationException(exception: OtpVerificationException): ResponseEntity<ErrorResponseBody> {
        logger.error("Otp verification error: ${exception.message}")
        val response = ErrorResponseBody.error(exception.message)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}
