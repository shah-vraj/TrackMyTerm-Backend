package com.trackmyterm.util

import com.trackmyterm.response.ForgotPasswordResponse
import com.trackmyterm.response.LoginResponse
import com.trackmyterm.response.OtpVerificationResponse
import com.trackmyterm.response.RegisterResponse
import com.trackmyterm.response.ResetPasswordResponse
import org.springframework.http.ResponseEntity

// ResponseEntity
typealias RegisterResponseEntity = ResponseEntity<RegisterResponse>
typealias LoginResponseEntity = ResponseEntity<LoginResponse>
typealias ForgotPasswordResponseEntity = ResponseEntity<ForgotPasswordResponse>
typealias OtpVerificationResponseEntity = ResponseEntity<OtpVerificationResponse>
typealias ResetPasswordResponseEntity = ResponseEntity<ResetPasswordResponse>
