package com.example.data.api

import com.example.data.model.ModelDto
import retrofit2.http.GET

interface Api {
    @GET("/data/holder")
    suspend fun getData(): List<ModelDto>
}