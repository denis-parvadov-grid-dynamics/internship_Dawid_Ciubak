package com.example.domain.repository

import com.example.domain.model.Model

interface Repository {
    suspend fun getData(): List<Model>
}
