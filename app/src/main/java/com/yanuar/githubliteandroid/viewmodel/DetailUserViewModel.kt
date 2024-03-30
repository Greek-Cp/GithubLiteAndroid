package com.yanuar.githubliteandroid.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanuar.githubliteandroid.data.database.UserFavDatabase
import com.yanuar.githubliteandroid.data.model.GithubDetailAccount
import com.yanuar.githubliteandroid.data.model.UserFav
import com.yanuar.githubliteandroid.data.remote.GithubApiService
import com.yanuar.githubliteandroid.data.remote.NetworkService
import com.yanuar.githubliteandroid.data.repository.UserFavRepository
import com.yanuar.githubliteandroid.util.Event
import kotlinx.coroutines.launch

class DetailUserViewModel(private val repository: UserFavRepository) : ViewModel() {
    private val _userDetail = MutableLiveData<GithubDetailAccount>()
    val userDetail: LiveData<GithubDetailAccount> = _userDetail
    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText
    private val githubApiService: GithubApiService = NetworkService.retrofit.create(GithubApiService::class.java)
    fun fetchUserDetail(username: String) {
        viewModelScope.launch {
            try {
                val response = githubApiService.getUserDetail(username)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _snackbarText.value = Event("Menampilkan Detail Akun $username")
                        _userDetail.postValue(it)
                    }
                } else {
                    _snackbarText.value = Event("Mohon Maaf Telah Terjadi Error Harap Pastikan Ponsel Anda Memiliki Akses Internet ${response.message()}")
                }
            } catch (e: Exception) {
                _snackbarText.value = Event("Mohon Maaf Telah Terjadi Error Harap Pastikan Ponsel Anda Memiliki Akses Internet ${e.message}")
            }
        }
    }
    private val _isUserFav = MutableLiveData<Boolean>()
    val isUserFav: LiveData<Boolean> = _isUserFav

    fun checkUserExistence(username: String) = viewModelScope.launch {
        val isAdded = repository.isUserAdded(username)
        _isUserFav.postValue(isAdded)
    }
    fun insertUser(userFav: UserFav) = viewModelScope.launch {
        repository.insert(userFav)
        checkUserExistence(userFav.username)
    }

    fun deleteUser(userFav: UserFav) = viewModelScope.launch {
        repository.delete(userFav)
        checkUserExistence(userFav.username)
    }

    companion object {
        fun create(context: Context): DetailUserViewModel {
            val userDao = UserFavDatabase.getDatabase(context).userDao()
            val userFavRepository = UserFavRepository(userDao)
            return DetailUserViewModel(userFavRepository)
        }
    }
}
