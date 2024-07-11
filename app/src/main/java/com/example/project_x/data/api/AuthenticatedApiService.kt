package com.example.project_x.data.api

import com.example.project_x.data.model.CourseResponse
import com.example.project_x.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface AuthenticatedApiService {

  @GET("users/profile")
  suspend fun getUserProfile(): Response<ProfileResponse>

  @GET("courses")
  suspend fun getCourses(): Response<CourseResponse>
}
