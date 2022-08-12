package com.example.data.remote.api

import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.model.ApiResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface FakeStoreApi {
    @POST("/auth/login")
    fun loginUser(@Body loginCredentials: LoginCredentialsWrapper): Single<ApiResponse>
}
