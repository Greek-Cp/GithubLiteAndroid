package com.yanuar.githubliteandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanuar.githubliteandroid.data.model.GithubUserFollower
import com.yanuar.githubliteandroid.data.model.GithubUserFollowerItem
import com.yanuar.githubliteandroid.data.model.ItemsItem
import com.yanuar.githubliteandroid.data.remote.GithubApiService
import com.yanuar.githubliteandroid.data.remote.NetworkService
import com.yanuar.githubliteandroid.util.Event
import kotlinx.coroutines.launch

class ListUserViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _userData = MutableLiveData<List<GithubUserFollowerItem>>()

    val userData: LiveData<List<GithubUserFollowerItem>> = _userData
    private val githubApiService: GithubApiService = NetworkService.retrofit.create(GithubApiService::class.java)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun fetchUserData(username: String, type: UserType) {
        _isLoading.postValue(true) // Loading dimulai

        viewModelScope.launch {
            try {
                val response = when (type) {
                    UserType.FOLLOWERS -> githubApiService.getFollowers(username)
                    UserType.FOLLOWING -> githubApiService.getFollowing(username)
                }
                if (response.isSuccessful) {
                    _userData.postValue(response.body())
                    _isLoading.postValue(false) // Loading dimulai

                } else {
                    Log.e("ViewModel", "Error fetching data: ${response.message()}")
                    _isLoading.postValue(false) // Loading dimulai

                }

            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching data: ${e.message}")
                _isLoading.postValue(false) // Loading dimulai

            }
        }
    }

    enum class UserType {
        FOLLOWERS, FOLLOWING
    }
}