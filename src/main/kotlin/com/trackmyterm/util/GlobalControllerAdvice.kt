package com.trackmyterm.util

import com.trackmyterm.exception.InvalidPasswordException
import com.trackmyterm.exception.UserAlreadyRegisteredException
import com.trackmyterm.exception.UserNotFoundException
import com.trackmyterm.response.ErrorResponseBody
import com.trackmyterm.util.Constants.GENERIC_API_ERROR_MESSAGE
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalControllerAdvice {

    private val logger = LoggerFactory.getLogger(GlobalControllerAdvice::class.java)

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationExceptions(
        exception: ConstraintViolationException
    ): ResponseEntity<ErrorResponseBody> {
        val errors = exception.constraintViolations.map { it.message }.toString()
        logger.error("Constraint violation error: $errors")
        val response = ErrorResponseBody.error(errors)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponseBody> {
        val errors = exception.bindingResult.fieldErrors.map { it.defaultMessage }.toString()
        logger.error("Method argument not valid error: $errors")
        val response = ErrorResponseBody.error(errors)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        exception: UserNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseBody> {
        logger.error("User not found: ${exception.message}")
        val response = ErrorResponseBody.error(exception.message.orEmpty())
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UserAlreadyRegisteredException::class)
    fun handleUserAlreadyRegisteredException(
        exception: UserAlreadyRegisteredException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseBody> {
        logger.error("User already registered: ${exception.message}")
        val response = ErrorResponseBody.error(exception.message.orEmpty())
        return ResponseEntity(response, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(InvalidPasswordException::class)
    fun handleUserAlreadyRegisteredException(
        exception: InvalidPasswordException,
        request: WebRequest
    ): ResponseEntity<ErrorResponseBody> {
        logger.error("User password does not match: ${exception.message}")
        val response = ErrorResponseBody.error(exception.message.orEmpty())
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        exception: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponseBody> {
        logger.error("An error occurred: ${exception.message}")
        val response = ErrorResponseBody.error(GENERIC_API_ERROR_MESSAGE)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
