package com.example.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val firstName: String = "", // set either on the account fragment or registration fragment
    val lastName: String = "", // set either on the account fragment or registration fragment
    val password: String
)
