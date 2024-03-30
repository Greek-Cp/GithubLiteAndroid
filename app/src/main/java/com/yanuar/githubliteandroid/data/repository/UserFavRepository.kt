package com.yanuar.githubliteandroid.data.repository

import com.yanuar.githubliteandroid.data.database.UserDao
import com.yanuar.githubliteandroid.data.model.UserFav
import kotlinx.coroutines.flow.Flow

class UserFavRepository(private val userDao: UserDao) {
    suspend fun insert(userFav: UserFav) = userDao.insertUser(userFav)
    suspend fun delete(userFav: UserFav) = userDao.deleteUser(userFav)
    suspend fun getFavoriteUsers(): List<UserFav> = userDao.getAllFavoriteUsers()
    fun searchFavoriteUsersByUsername(username: String): Flow<List<UserFav>> {
        return userDao.searchFavoriteUsersByUsername(username)
    }
    suspend fun isUserFavExists(username: String): Boolean = userDao.isUserFavExists(username)
    suspend fun isUserAdded(username: String): Boolean {
        return userDao.countUserByUsername(username) > 0
    }

}
