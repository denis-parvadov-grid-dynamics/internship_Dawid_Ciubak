package com.example.data.repository

import com.example.data.api.Api
import com.example.data.model.toModel
import com.example.domain.model.Model
import com.example.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: Api
) : Repository {
    override suspend fun getData(): List<Model> {
        return api.getData().map { it.toModel() }
    }
}
