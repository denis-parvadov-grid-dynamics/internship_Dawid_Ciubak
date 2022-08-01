package com.example.data.local.repository

import com.example.data.local.dao.DatabaseDao
import com.example.domain.model.User
import com.example.domain.repository.AppDatabaseRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AppDatabaseRepositoryImpl @Inject constructor(
    private val dao: DatabaseDao
) : AppDatabaseRepository {
    override fun checkForLoggedUser(): Single<User> {
        return dao.checkForLoggedUser()
    }

    override fun updateUser(user: User): Completable {
        return dao.updateUser(user)
    }

    override fun getUserWithMatchingCredentials(username: String, password: String): Single<User> {
        return dao.getUserWithMatchingCredentials(username, password)
    }

    override fun saveUserInDatabase(user: User): Completable {
        return dao.saveUserInDatabase(user)
    }
}
