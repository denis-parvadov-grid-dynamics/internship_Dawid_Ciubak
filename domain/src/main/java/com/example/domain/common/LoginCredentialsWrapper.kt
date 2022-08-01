package com.example.domain.common

// data class used to send user login cred to the API
data class LoginCredentialsWrapper(
    val username: String,
    val password: String
)
