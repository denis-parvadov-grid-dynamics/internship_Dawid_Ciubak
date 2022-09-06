package com.example.domain.model.api_model

data class Address(
    val city: String,
    val geolocation: Geolocation,
    val number: String,
    val street: String
)
