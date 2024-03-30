package com.yanuar.githubliteandroid.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yanuar.githubliteandroid.data.model.UserFav
import kotlinx.coroutines.flow.Flow
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(userFav: UserFav)
    @Delete
    suspend fun deleteUser(userFav: UserFav)
    @Query("SELECT * FROM UserFav")
    fun getAllFavoriteUsers(): Flow<List<UserFav>>

    @Query("SELECT * FROM UserFav WHERE username LIKE :username")
    fun searchFavoriteUsersByUsername(username: String): Flow<List<UserFav>>
    @Query("SELECT EXISTS(SELECT * FROM UserFav WHERE username = :username)")
    suspend fun isUserFavExists(username: String): Boolean
    @Query("SELECT COUNT(username) FROM UserFav WHERE username = :username")
    suspend fun countUserByUsername(username: String): Int


}
