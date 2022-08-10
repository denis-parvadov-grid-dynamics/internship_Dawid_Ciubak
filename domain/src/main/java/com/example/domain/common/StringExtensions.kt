package com.example.domain.common

fun String.validateAsEmail(): String? {
    val emailRegex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    if (!this.matches(emailRegex)) {
        return "Invalid Email Address"
    }
    return null
}

fun String.validateAsPassword(): String? {
    if (this.length < 6) {
        return "Minimum 6 Character Password"
    }
    return null
}

// used both for first name and last name
fun String.validateAsName(): String? {
    if (this.length < 3) {
        return "Not a valid name"
    }
    return null
}
