package com.yanuar.githubliteandroid.data.remote

import com.yanuar.githubliteandroid.data.model.GithubDetailAccount
import com.yanuar.githubliteandroid.data.model.GithubSearchResponse
import com.yanuar.githubliteandroid.data.model.GithubUserFollower
import com.yanuar.githubliteandroid.data.model.GithubUserFollowerItem
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GithubApiService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): Response<GithubSearchResponse>
    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): Response<GithubDetailAccount>
    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Response<List<GithubUserFollowerItem>>
    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Response<List<GithubUserFollowerItem>>
}