package com.example.data.di

import com.example.data.api.Api
import com.example.data.repository.RepositoryImpl
import com.example.domain.common.Constants
import com.example.domain.repository.Repository
import com.example.domain.use_case.getData.GetDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideApi(): Api = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

    @Provides
    @Singleton
    fun provideRepository(api: Api): Repository = RepositoryImpl(api)

    @Provides
    @Singleton
    fun getDataUseCase(repository: RepositoryImpl): GetDataUseCase = GetDataUseCase(repository)
}