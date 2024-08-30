package com.trackmyterm.repository

import com.trackmyterm.model.Otp
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface OtpRepository: MongoRepository<Otp, String> {

    fun findByEmail(email: String): Otp?
}
