package com.yanuar.githubliteandroid.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanuar.githubliteandroid.data.database.UserFavDatabase
import com.yanuar.githubliteandroid.data.model.UserFav
import com.yanuar.githubliteandroid.data.repository.UserFavRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritUserViewModel(private val repository: UserFavRepository) : ViewModel() {

    private val _favoriteUsers = MutableStateFlow<List<UserFav>>(emptyList())
    val favoriteUsers: StateFlow<List<UserFav>> = _favoriteUsers.asStateFlow()


    init {
        loadFavoriteUsers()
    }

    private fun loadFavoriteUsers() = viewModelScope.launch {
        repository.getFavoriteUsers().collect { users ->
            _favoriteUsers.value = users
        }
    }
    fun searchFavoriteUsers(username: String) = viewModelScope.launch {
        val query = "%$username%"
        repository.searchFavoriteUsersByUsername(query).collect { searchResults ->
            _favoriteUsers.value = searchResults
        }
    }
    companion object {
        fun create(context: Context): FavoritUserViewModel {
            val userDao = UserFavDatabase.getDatabase(context).userDao()
            val repository = UserFavRepository(userDao)
            return FavoritUserViewModel(repository)
        }
    }
}