package com.example.project_x.data.api

import com.example.project_x.data.model.CourseResponse
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticatedApiService {

  @GET("users/profile")
  suspend fun getUserProfile(): Response<ProfileResponse>

  @GET("courses")
  suspend fun getCourses(): Response<List<CourseResponse>>

  @POST("users/refresh-token")
  suspend fun refreshToken(@Body refreshToken: String): Response<TokenResponse>
}
