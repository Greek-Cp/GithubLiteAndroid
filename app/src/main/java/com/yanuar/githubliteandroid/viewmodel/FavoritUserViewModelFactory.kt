package com.yanuar.githubliteandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanuar.githubliteandroid.data.repository.UserFavRepository

class FavoritUserViewModelFactory(private val repository: UserFavRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
