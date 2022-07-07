package com.example.internshipappdawidciubak.util

// wrapper class to help with handling the states of data (retrofit related)
sealed class Resource<T>(val data: T? = null, val errorMessage: String? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(errorMessage: String, data: T? = null): Resource<T>(data, errorMessage)
    class Loading<T>(data: T? = null): Resource<T>(data)
}