package com.example.domain.repository

import com.example.domain.model.user_related.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface AppDatabaseRepository {
    fun checkForLoggedUser(): Single<User>
    fun updateUser(user: User): Completable
    fun getUserWithMatchingCredentials(username: String, password: String): Single<User>
    fun saveUserInDatabase(user: User): Completable
    fun deleteAllUserRecords(): Completable
}
