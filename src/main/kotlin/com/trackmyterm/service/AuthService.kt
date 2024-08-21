package com.trackmyterm.service

import com.trackmyterm.exception.UserAlreadyRegisteredException
import com.trackmyterm.repository.UserRepository
import com.trackmyterm.request.RegisterRequest
import com.trackmyterm.util.Constants.REGISTER_REQUEST_SUCCESS
import com.trackmyterm.util.ResponseBody
import com.trackmyterm.util.ResponseBody.ResultType.SUCCESS
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface AuthService {

    /**
     * Register a user into the application
     * @param registerRequest Request model for registering the user
     */
    fun registerUser(registerRequest: RegisterRequest): ResponseBody<Boolean>
}

@Service
class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : AuthService {

    override fun registerUser(registerRequest: RegisterRequest): ResponseBody<Boolean> {
        if (userRepository.existsByEmail(registerRequest.email))
            throw UserAlreadyRegisteredException(registerRequest.email)

        val user = registerRequest.toUser(passwordEncoder)
        userRepository.save(user)
        return ResponseBody(SUCCESS, true, REGISTER_REQUEST_SUCCESS)
    }
}
