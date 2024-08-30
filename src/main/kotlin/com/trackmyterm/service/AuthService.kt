package com.trackmyterm.service

import com.trackmyterm.exception.InvalidPasswordException
import com.trackmyterm.exception.UserAlreadyRegisteredException
import com.trackmyterm.exception.UserNotFoundException
import com.trackmyterm.model.Otp
import com.trackmyterm.repository.OtpRepository
import com.trackmyterm.repository.UserRepository
import com.trackmyterm.request.ForgotPasswordRequest
import com.trackmyterm.request.LoginRequest
import com.trackmyterm.request.RegisterRequest
import com.trackmyterm.response.ForgotPasswordResponse
import com.trackmyterm.response.LoginResponse
import com.trackmyterm.response.LoginResponse.Data
import com.trackmyterm.response.RegisterResponse
import com.trackmyterm.util.EmailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthService {

    /**
     * Register a user into the application
     * @param registerRequest Request model for registering the user
     * @return RegisterResponse object
     */
    fun registerUser(registerRequest: RegisterRequest): RegisterResponse

    /**
     * Login the user into the application
     * @param loginRequest User details to login
     * @return LoginResponse object
     */
    fun loginUser(loginRequest: LoginRequest): LoginResponse

    /**
     * Handles forgot password request and sends OTP to the requesting email address
     * @param forgotPasswordRequest Request model containing email to make a password change request
     * @return ForgotPasswordResponse object
     */
    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse
}

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val otpRepository: OtpRepository,
    private val emailSender: EmailSender
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

    override fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest): ForgotPasswordResponse {
        otpRepository.findByEmail(forgotPasswordRequest.email)?.let(otpRepository::delete)
        val otp = Otp.getOtpModel(forgotPasswordRequest.email)
        otpRepository.save(otp).also {
            emailSender.sendOtpToMail(it.otp, it.email)
        }
        return ForgotPasswordResponse.success()
    }
}
