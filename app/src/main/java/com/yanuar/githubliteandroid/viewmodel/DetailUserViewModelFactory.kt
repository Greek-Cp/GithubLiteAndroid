package com.yanuar.githubliteandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yanuar.githubliteandroid.data.repository.UserFavRepository
class DetailUserViewModelFactory(private val repository: UserFavRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailUserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}