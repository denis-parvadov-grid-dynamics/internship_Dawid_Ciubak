package com.example.data.remote.repository

import com.example.data.remote.api.FakeStoreApi
import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.model.ApiResponse
import com.example.domain.repository.FakeStoreRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FakeStoreRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi
) : FakeStoreRepository {
    override fun loginUser(loginCredentials: LoginCredentialsWrapper): Single<ApiResponse> {
        return api.loginUser(loginCredentials)
    }
}
