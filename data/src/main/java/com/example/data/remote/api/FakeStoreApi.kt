package com.example.data.remote.api

import com.example.domain.common.LoginCredentialsWrapper
import com.example.domain.common.SortOrder
import com.example.domain.model.product_related.ProductModel
import com.example.domain.model.user_related.ApiResponse
import com.example.domain.model.user_related.api_model.ApiUser
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FakeStoreApi {
    @POST("/auth/login")
    fun loginUser(@Body loginCredentials: LoginCredentialsWrapper): Single<ApiResponse>

    // normally we would just pass the user id to the function, use it in the query and
    // make the call, however the api probably will break if the user's id > 20
    // and also, this api won't store our changes anyway, so we just "update" the 7th user
    // to prevent errors on the api's side
    @PUT("/users/7")
    fun updateUser(@Body user: ApiUser): Completable

    @GET("/products")
    fun getAllProducts(@Query("sort") sort: SortOrder): Observable<List<ProductModel>>
}
