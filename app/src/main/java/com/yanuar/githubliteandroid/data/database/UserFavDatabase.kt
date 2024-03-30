package com.yanuar.githubliteandroid.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yanuar.githubliteandroid.data.model.UserFav
@Database(entities = [UserFav::class], version = 1)
abstract class UserFavDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        @Volatile
        private var INSTANCE: UserFavDatabase? = null
        fun getDatabase(context: Context): UserFavDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserFavDatabase::class.java,
                    "user_fav_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
