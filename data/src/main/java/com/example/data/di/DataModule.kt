package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.local.dao.DatabaseDao
import com.example.data.local.repository.AppDatabaseRepositoryImpl
import com.example.data.remote.api.FakeStoreApi
import com.example.data.remote.interceptors.ConnectivityInterceptor
import com.example.data.remote.repository.FakeStoreRepositoryImpl
import com.example.domain.common.Constants
import com.example.domain.repository.AppDatabaseRepository
import com.example.domain.repository.FakeStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideConnectivityInterceptor() = ConnectivityInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(connectivityInterceptor: ConnectivityInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(connectivityInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient): FakeStoreApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(FakeStoreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFakeStoreRepository(api: FakeStoreApi): FakeStoreRepository {
        return FakeStoreRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room
            .databaseBuilder(appContext, AppDatabase::class.java, "appDatabase.db")
            .build()
    }

    @Provides
    fun provideDatabaseDao(db: AppDatabase): DatabaseDao = db.getDatabaseDao()

    @Provides
    @Singleton
    fun provideAppDatabaseRepository(dao: DatabaseDao): AppDatabaseRepository {
        return AppDatabaseRepositoryImpl(dao)
    }
}
