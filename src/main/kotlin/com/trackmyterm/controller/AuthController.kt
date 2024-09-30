package com.trackmyterm.controller

import com.trackmyterm.request.ForgotPasswordRequest
import com.trackmyterm.request.LoginRequest
import com.trackmyterm.request.OtpVerificationRequest
import com.trackmyterm.request.RegisterRequest
import com.trackmyterm.response.ForgotPasswordResponse
import com.trackmyterm.response.LoginResponse
import com.trackmyterm.response.OtpVerificationResponse
import com.trackmyterm.response.RegisterResponse
import com.trackmyterm.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<RegisterResponse> {
        val body = authService.registerUser(registerRequest)
        return ResponseEntity(body, HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val body = authService.loginUser(loginRequest)
        return ResponseEntity(body, HttpStatus.OK)
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(
        @Valid @RequestBody forgotPasswordRequest: ForgotPasswordRequest
    ): ResponseEntity<ForgotPasswordResponse> {
        val body = authService.forgotPassword(forgotPasswordRequest)
        return ResponseEntity(body, HttpStatus.OK)
    }

    @PostMapping("/verify-otp")
    fun verifyOtp(
        @Valid @RequestBody otpVerificationRequest: OtpVerificationRequest
    ): ResponseEntity<OtpVerificationResponse> {
        val body = authService.verifyOtp(otpVerificationRequest)
        return ResponseEntity(body, HttpStatus.OK)
    }
}
