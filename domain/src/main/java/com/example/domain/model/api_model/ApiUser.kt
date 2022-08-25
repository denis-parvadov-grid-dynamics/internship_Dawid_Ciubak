package com.example.domain.model.api_model

// a dataclass that our API accepts, the API will throw 500 if any of the fields are missing
// which isn't helpful because we don't use address, and we use a profile picture and ID
data class ApiUser(
    val address: Address,
    val email: String,
    val name: Name,
    val password: String,
    val phone: String,
    val username: String
)
