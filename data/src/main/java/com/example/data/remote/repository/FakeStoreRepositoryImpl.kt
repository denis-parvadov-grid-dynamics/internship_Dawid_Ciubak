package com.example.data.remote.repository

import com.example.data.remote.api.FakeStoreApi
import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.common.SortOrder
import com.example.domain.model.product_related.ProductModel
import com.example.domain.model.user_related.ApiResponse
import com.example.domain.model.user_related.api_model.ApiUser
import com.example.domain.repository.FakeStoreRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FakeStoreRepositoryImpl @Inject constructor(
    private val api: FakeStoreApi
) : FakeStoreRepository {
    override fun loginUser(loginCredentials: LoginCredentialsWrapper): Single<ApiResponse> {
        return api.loginUser(loginCredentials)
    }

    // unfortunately the API always returns 500 error code if there is some missing data
    // (missing geolocation, which we don't use in our app)
    // or too much data (a profile picture, which we use in our app)
    // that's why we are using "UpdateUser" and not our app's "User"
    override fun updateUser(user: ApiUser): Completable {
        return api.updateUser(user)
    }

    override fun getAllProducts(sort: SortOrder): Observable<List<ProductModel>> {
        return api.getAllProducts(sort)
    }
}
