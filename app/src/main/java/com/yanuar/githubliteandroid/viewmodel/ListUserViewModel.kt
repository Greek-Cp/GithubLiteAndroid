package com.yanuar.githubliteandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanuar.githubliteandroid.data.model.GithubUserFollowerItem
import com.yanuar.githubliteandroid.data.remote.GithubApiService
import com.yanuar.githubliteandroid.data.remote.NetworkService
import com.yanuar.githubliteandroid.util.Event
import kotlinx.coroutines.launch

class ListUserViewModel : ViewModel() {
    private val _userData = MutableLiveData<List<GithubUserFollowerItem>>()
    val userData: LiveData<List<GithubUserFollowerItem>> = _userData
    private val githubApiService: GithubApiService = NetworkService.retrofit.create(GithubApiService::class.java)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText
    fun fetchUserData(username: String, type: UserType) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = when (type) {
                    UserType.FOLLOWERS -> githubApiService.getFollowers(username)
                    UserType.FOLLOWING -> githubApiService.getFollowing(username)
                }
                if (response.isSuccessful) {
                    _userData.postValue(response.body())
                    _isLoading.postValue(false)
                } else {
                    Log.e("Error", response.message())
                    _snackbarText.value = Event("Mohon Maaf Telah Terjadi Error ${response.message()}")
                    _isLoading.postValue(true)
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                _snackbarText.value = Event("Mohon Maaf Telah Terjadi Error ${e.message}")
                _isLoading.postValue(true)
            }
        }
    }
    enum class UserType {
        FOLLOWERS, FOLLOWING
    }
}