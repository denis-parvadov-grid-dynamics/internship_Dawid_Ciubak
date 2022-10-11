package com.example.domain.model.user_related

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.user_related.api_model.Address
import com.example.domain.model.user_related.api_model.ApiUser
import com.example.domain.model.user_related.api_model.Geolocation
import com.example.domain.model.user_related.api_model.Name

@Entity(tableName = "all_users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val profilePicture: Uri = Uri.EMPTY, // set on the account fragment
    val firstName: String = "", // set either on the account fragment or registration fragment
    val lastName: String = "", // set either on the account fragment or registration fragment
    val password: String
) {
    // basically convert our apps user entity into the api's desired entity (take a look at
    // the comments left in "ApiUser")
    fun toUpdateUser(): ApiUser {
        return ApiUser(
            address = Address(city = "placeholder", geolocation = Geolocation("0.0", "0.0"), number = "404", street = "placeholder"),
            email = "placeholder",
            name = Name(firstname = firstName, lastname = lastName),
            password = password,
            phone = "000-000",
            username = username
        )
    }
}
