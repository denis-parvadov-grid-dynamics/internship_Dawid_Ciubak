package com.example.domain.repository

import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.model.ApiResponse
import io.reactivex.rxjava3.core.Single

interface FakeStoreRepository {
    fun loginUser(loginCredentials: LoginCredentialsWrapper): Single<ApiResponse>
}
