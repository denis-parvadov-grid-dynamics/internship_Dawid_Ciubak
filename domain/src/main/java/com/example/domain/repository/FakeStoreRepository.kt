package com.example.domain.repository

import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.common.SortOrder
import com.example.domain.model.product_related.ProductModel
import com.example.domain.model.user_related.ApiResponse
import com.example.domain.model.user_related.api_model.ApiUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface FakeStoreRepository {
    fun loginUser(loginCredentials: LoginCredentialsWrapper): Single<ApiResponse>
    fun updateUser(user: ApiUser): Completable
    fun getAllProducts(sort: SortOrder): Observable<List<ProductModel>>
}
