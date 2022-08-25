package com.example.domain.repository

import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.model.ApiResponse
import com.example.domain.model.api_model.ApiUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FakeStoreRepository {
    fun loginUser(loginCredentials: LoginCredentialsWrapper): Single<ApiResponse>
    fun updateUser(user: ApiUser): Completable
}
