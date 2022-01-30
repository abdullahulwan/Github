package com.dicoding.github

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUserList(
        @Query("q") username: String,
        @Header("Authorization") token: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<UserDetail>

    @GET("users/{username}/followers")
    fun getUserFollower(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<FollowResponseItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<FollowResponseItem>>
}