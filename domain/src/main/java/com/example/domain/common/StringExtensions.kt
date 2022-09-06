package com.example.domain.common

import android.text.Editable

fun String.validateAsUsername(): String? {
    if (this.isEmpty()) {
        return "Minimum 1 Character Username"
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

fun Editable?.toTrimmedString(): String {
    return this.toString().trim()
}
