package com.trackmyterm.util

import com.trackmyterm.exception.UserAlreadyRegisteredException
import com.trackmyterm.exception.UserNotFoundException
import com.trackmyterm.util.ResponseBody.ResultType.FAILURE
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
    ): ResponseEntity<ResponseBody<Boolean>> {
        val errors = exception.constraintViolations.map { it.message }.toString()
        logger.error("Constraint violation error: $errors")
        val response = ResponseBody(FAILURE, false, errors)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<ResponseBody<Boolean>> {
        val errors = exception.bindingResult.fieldErrors.map { it.defaultMessage }.toString()
        logger.error("Method argument not valid error: $errors")
        val response = ResponseBody(FAILURE, false, errors)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        exception: UserNotFoundException,
        request: WebRequest
    ): ResponseEntity<ResponseBody<Boolean>> {
        logger.error("User not found: ${exception.message}")
        val response = ResponseBody(FAILURE, false, exception.message.orEmpty())
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UserAlreadyRegisteredException::class)
    fun handleUserAlreadyRegisteredException(
        exception: UserAlreadyRegisteredException,
        request: WebRequest
    ): ResponseEntity<ResponseBody<Boolean>> {
        logger.error("User already registered: ${exception.message}")
        val response = ResponseBody(FAILURE, false, exception.message.orEmpty())
        return ResponseEntity(response, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        exception: Exception,
        request: WebRequest
    ): ResponseEntity<ResponseBody<Boolean>> {
        logger.error("An error occurred: ${exception.message}")
        val response = ResponseBody(FAILURE, false, "An unexpected error occurred.")
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
