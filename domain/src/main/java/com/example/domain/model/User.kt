package com.example.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val token: String? = null // obtained from UserLoginToken received from API, empty after registration
)
