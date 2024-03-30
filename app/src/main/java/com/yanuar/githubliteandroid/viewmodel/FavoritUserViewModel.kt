package com.yanuar.githubliteandroid.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanuar.githubliteandroid.data.database.UserFavDatabase
import com.yanuar.githubliteandroid.data.model.UserFav
import com.yanuar.githubliteandroid.data.repository.UserFavRepository
import kotlinx.coroutines.launch

class FavoritUserViewModel(private val repository: UserFavRepository) : ViewModel() {

    private val _favoriteUsers = MutableLiveData<List<UserFav>>()
    val favoriteUsers: LiveData<List<UserFav>> = _favoriteUsers
    private val _isUserFav = MutableLiveData<Boolean>()
    val isUserFav: LiveData<Boolean> = _isUserFav


    init {
        loadFavoriteUsers()
    }
    fun checkUserExistence(username: String) = viewModelScope.launch {
        _isUserFav.value = repository.isUserFavExists(username)
    }
    private fun loadFavoriteUsers() = viewModelScope.launch {
        _favoriteUsers.value = repository.getFavoriteUsers()
    }
    fun searchFavoriteUsers(username: String) = viewModelScope.launch {
        val query = "%$username%"
        repository.searchFavoriteUsersByUsername(query).collect { searchResults ->
            _favoriteUsers.value = searchResults
        }
    }

    fun insertUser(userFav: UserFav) = viewModelScope.launch {
        repository.insert(userFav)
        loadFavoriteUsers() // Muat ulang daftar pengguna favorit setelah menambahkan baru
    }
    companion object {
        fun create(context: Context): FavoritUserViewModel {
            val userDao = UserFavDatabase.getDatabase(context).userDao()
            val repository = UserFavRepository(userDao)
            return FavoritUserViewModel(repository)
        }
    }
}