package com.trackmyterm.controller

import com.trackmyterm.request.LoginRequest
import com.trackmyterm.request.RegisterRequest
import com.trackmyterm.response.LoginResponse
import com.trackmyterm.service.AuthService
import com.trackmyterm.util.ResponseBody
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
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<ResponseBody<Boolean>> {
        val body = authService.registerUser(registerRequest)
        return ResponseEntity(body, HttpStatus.CREATED)
    }

    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<ResponseBody<LoginResponse>> {
        val body = authService.loginUser(loginRequest)
        return ResponseEntity(body, HttpStatus.OK)
    }
}
