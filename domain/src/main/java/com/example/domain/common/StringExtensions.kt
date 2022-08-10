package com.example.domain.common

fun String.validateAsUsername(): String? {
    if(this.length < 2) {
        return "Minimum 2 Character Username"
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
