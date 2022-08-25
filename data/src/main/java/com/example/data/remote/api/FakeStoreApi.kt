package com.example.data.remote.api

import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.model.ApiResponse
import com.example.domain.model.api_model.ApiUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface FakeStoreApi {
    @POST("/auth/login")
    fun loginUser(@Body loginCredentials: LoginCredentialsWrapper): Single<ApiResponse>

    @PUT("/users/7")
    fun updateUser(@Body user: ApiUser): Completable
}
