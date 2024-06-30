package com.example.project_x.data.api

import com.example.project_x.data.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
  @POST("register") suspend fun registerUser(@Body user: User): User
}
