package com.example.domain.repository

import com.example.domain.model.Model
import dagger.Module

interface Repository {
    suspend fun getData(): List<Model>
}