package com.trackmyterm.controller

import com.trackmyterm.request.ForgotPasswordRequest
import com.trackmyterm.request.LoginRequest
import com.trackmyterm.request.OtpVerificationRequest
import com.trackmyterm.request.RegisterRequest
import com.trackmyterm.request.ResetPasswordRequest
import com.trackmyterm.service.AuthService
import com.trackmyterm.util.ForgotPasswordResponseEntity
import com.trackmyterm.util.LoginResponseEntity
import com.trackmyterm.util.OtpVerificationResponseEntity
import com.trackmyterm.util.RegisterResponseEntity
import com.trackmyterm.util.ResetPasswordResponseEntity
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): RegisterResponseEntity {
        val body = authService.registerUser(request)
        return ResponseEntity.ok(body)
    }

    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody request: LoginRequest): LoginResponseEntity {
        val body = authService.loginUser(request)
        return ResponseEntity.ok(body)
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@Valid @RequestBody request: ForgotPasswordRequest): ForgotPasswordResponseEntity {
        val body = authService.forgotPassword(request)
        return ResponseEntity.ok(body)
    }

    @PostMapping("/verify-otp")
    fun verifyOtp(@Valid @RequestBody request: OtpVerificationRequest): OtpVerificationResponseEntity {
        val body = authService.verifyOtp(request)
        return ResponseEntity.ok(body)
    }

    @PostMapping("reset-password")
    fun resetPassword(@Valid @RequestBody request: ResetPasswordRequest): ResetPasswordResponseEntity {
        val body = authService.resetPassword(request)
        return ResponseEntity.ok(body)
    }
}
