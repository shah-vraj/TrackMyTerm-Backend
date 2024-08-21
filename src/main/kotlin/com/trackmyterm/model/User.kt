package com.trackmyterm.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
data class User (
    @Id
    val id: String,

    @Field(name = "username")
    val userName: String,

    @Field(name = "email")
    val email: String,

    @Field(name = "password")
    val userPassword: String
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getPassword(): String = userPassword

    override fun getUsername(): String = email
}
