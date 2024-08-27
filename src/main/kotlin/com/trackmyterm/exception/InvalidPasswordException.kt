package com.trackmyterm.exception

class InvalidPasswordException(email: String) :
    RuntimeException("Invalid password provided for email: $email")
