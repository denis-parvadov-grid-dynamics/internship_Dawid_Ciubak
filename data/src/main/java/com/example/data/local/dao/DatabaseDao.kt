package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.domain.model.user_related.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM all_users")
    fun checkForLoggedUser(): Single<User>

    @Update
    fun updateUser(user: User): Completable

    @Query("SELECT * FROM all_users WHERE username = :username AND password = :password")
    fun getUserWithMatchingCredentials(username: String, password: String): Single<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveUserInDatabase(user: User): Completable

    @Query("DELETE FROM all_users")
    fun deleteAllUserRecords(): Completable
}
