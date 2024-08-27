package com.trackmyterm.service

import com.trackmyterm.exception.InvalidPasswordException
import com.trackmyterm.exception.UserAlreadyRegisteredException
import com.trackmyterm.exception.UserNotFoundException
import com.trackmyterm.repository.UserRepository
import com.trackmyterm.request.LoginRequest
import com.trackmyterm.request.RegisterRequest
import com.trackmyterm.response.LoginResponse
import com.trackmyterm.response.LoginResponse.Data
import com.trackmyterm.response.RegisterResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthService {

    /**
     * Register a user into the application
     * @param registerRequest Request model for registering the user
     * @return True if user registration is successful, False otherwise
     */
    fun registerUser(registerRequest: RegisterRequest): RegisterResponse

    /**
     * Login the user into the application
     * @param loginRequest User details to login
     * @return ResponseBody of LoginResponse containing token
     */
    fun loginUser(loginRequest: LoginRequest): LoginResponse
}

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) : AuthService {

    override fun registerUser(registerRequest: RegisterRequest): RegisterResponse {
        if (userRepository.existsByEmail(registerRequest.email))
            throw UserAlreadyRegisteredException(registerRequest.email)

        val user = registerRequest.toUser(passwordEncoder)
        userRepository.save(user)
        return RegisterResponse.success()
    }

    override fun loginUser(loginRequest: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw UserNotFoundException(loginRequest.email)
        if (!passwordEncoder.matches(loginRequest.password, user.password))
            throw InvalidPasswordException(loginRequest.email)

        val token = jwtService.generateToken(user)
        return LoginResponse.success(Data(token))
    }
}
