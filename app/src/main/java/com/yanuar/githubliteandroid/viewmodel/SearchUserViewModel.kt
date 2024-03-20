package com.yanuar.githubliteandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanuar.githubliteandroid.data.model.GithubSearchResponse
import com.yanuar.githubliteandroid.data.remote.GithubApiService
import com.yanuar.githubliteandroid.data.remote.NetworkService
import com.yanuar.githubliteandroid.util.Event
import kotlinx.coroutines.launch


class SearchUserViewModel() : ViewModel() {
    private val _searchResult = MutableLiveData<GithubSearchResponse>()
    val searchResults: LiveData<GithubSearchResponse> = _searchResult
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText
    private val githubApiService: GithubApiService = NetworkService.retrofit.create(GithubApiService::class.java)
    fun cariUsers(query: String) {
        _isLoading.postValue(true) // Loading dimulai
        viewModelScope.launch {
            try {
                val response = githubApiService.searchUsers(query)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _searchResult.postValue(it)
                        _isLoading.postValue(false)
                        _snackbarText.value = Event("Mencari User Dengan ${query}")
                    }
                } else {
                    // Handle API error response
                    Log.d("D:", response.message())
                    _isLoading.postValue(false)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false) // Loading selesai
                Log.e("Error: ", e.message.toString());


            }
        }
    }
}