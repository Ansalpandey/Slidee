package com.example.project_x.data.api

import com.example.project_x.data.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
  @POST("users/register") suspend fun registerUser(@Body user: User): User

  @POST("users/login") suspend fun loginUser(@Body user: User): User
}
