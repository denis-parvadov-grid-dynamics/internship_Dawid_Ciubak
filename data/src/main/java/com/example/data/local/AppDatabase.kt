package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.dao.DatabaseDao
import com.example.domain.common.UriConverters
import com.example.domain.model.user_related.User

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(UriConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDatabaseDao(): DatabaseDao
}
