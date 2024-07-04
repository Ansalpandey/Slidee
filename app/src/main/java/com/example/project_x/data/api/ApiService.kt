package com.example.project_x.data.api

import com.example.project_x.data.model.Profile
import com.example.project_x.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
  @POST("users/register") suspend fun registerUser(@Body user: User): Response<User>

  @POST("users/login") suspend fun loginUser(@Body user: User): Response<User>

  @POST("users/logout") suspend fun logoutUser(): Response<User>

  @GET("users")
  suspend fun getUserProfile(@Header("AccessToken") accessToken: String): Response<Profile>
}
