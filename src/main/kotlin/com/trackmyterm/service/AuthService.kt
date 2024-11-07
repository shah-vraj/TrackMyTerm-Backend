package com.trackmyterm.service

import com.trackmyterm.exception.InvalidPasswordException
import com.trackmyterm.exception.OtpVerificationException
import com.trackmyterm.exception.UserAlreadyRegisteredException
import com.trackmyterm.exception.UserNotFoundException
import com.trackmyterm.model.Otp
import com.trackmyterm.repository.OtpRepository
import com.trackmyterm.repository.UserRepository
import com.trackmyterm.request.ForgotPasswordRequest
import com.trackmyterm.request.LoginRequest
import com.trackmyterm.request.OtpVerificationRequest
import com.trackmyterm.request.RegisterRequest
import com.trackmyterm.request.ResetPasswordRequest
import com.trackmyterm.response.ForgotPasswordResponse
import com.trackmyterm.response.LoginResponse
import com.trackmyterm.response.LoginResponse.Data
import com.trackmyterm.response.OtpVerificationResponse
import com.trackmyterm.response.RegisterResponse
import com.trackmyterm.response.ResetPasswordResponse
import com.trackmyterm.util.EmailSender
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface AuthService {

    /**
     * Register a user into the application
     * @param request Request model for registering the user
     * @return [RegisterResponse] object
     */
    fun registerUser(request: RegisterRequest): RegisterResponse

    /**
     * Login the user into the application
     * @param request User details to login
     * @return [LoginResponse] object
     */
    fun loginUser(request: LoginRequest): LoginResponse

    /**
     * Handles forgot password request and sends OTP to the requesting email address
     * @param request Request model containing email to make a password change request
     * @return [ForgotPasswordResponse] object
     */
    fun forgotPassword(request: ForgotPasswordRequest): ForgotPasswordResponse

    /**
     * Handles verifying otp request
     * @param request Request model containing necessary details for verifying otp
     * @return [OtpVerificationResponse] object
     */
    fun verifyOtp(request: OtpVerificationRequest): OtpVerificationResponse

    /**
     * Handles reset password request
     * @param request Request model containing necessary details for resetting password
     * @return [ResetPasswordResponse] object
     */
    fun resetPassword(request: ResetPasswordRequest): ResetPasswordResponse
}

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val otpRepository: OtpRepository,
    private val emailSender: EmailSender
) : AuthService {

    override fun registerUser(request: RegisterRequest): RegisterResponse {
        if (userRepository.existsByEmail(request.email))
            throw UserAlreadyRegisteredException(request.email)

        val user = request.toUser(passwordEncoder)
        userRepository.save(user)
        return RegisterResponse.success()
    }

    override fun loginUser(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException(request.email)
        if (!passwordEncoder.matches(request.password, user.password))
            throw InvalidPasswordException(request.email)

        val token = jwtService.generateToken(user)
        return LoginResponse.success(Data(token))
    }

    override fun forgotPassword(request: ForgotPasswordRequest): ForgotPasswordResponse {
        val email = request.email
        if (!userRepository.existsByEmail(email))
            throw UserNotFoundException(email)

        otpRepository.findByEmail(email)?.let(otpRepository::delete)
        val otp = Otp.getOtpModel(email)
        otpRepository.save(otp).also {
            emailSender.sendOtpToMail(it.otp, it.email)
        }
        return ForgotPasswordResponse.success()
    }

    override fun verifyOtp(request: OtpVerificationRequest): OtpVerificationResponse {
        val email = request.email
        if (!userRepository.existsByEmail(email))
            throw UserNotFoundException(email)

        val otp = otpRepository.findByEmail(email) ?: throw OtpVerificationException.EmailNotFound(email)
        when {
            otp.expires.isBefore(LocalDateTime.now()) -> throw OtpVerificationException.OtpExpired(email)
            otp.otp != request.otp -> throw OtpVerificationException.OtpNotMatching(email)
        }
        otpRepository.delete(otp)
        return OtpVerificationResponse.success()
    }

    override fun resetPassword(request: ResetPasswordRequest): ResetPasswordResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException(request.email)

        val userWithUpdatedPassword = user.copy(userPassword = passwordEncoder.encode(request.password))
        userRepository.save(userWithUpdatedPassword)
        return ResetPasswordResponse.success()
    }
}
