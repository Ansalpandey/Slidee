package com.example.project_x.data.api

import com.example.project_x.data.model.CourseResponse
import com.example.project_x.data.model.PostRequest
import com.example.project_x.data.model.PostResponse
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.model.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticatedApiService {

  @GET("users/profile")
  suspend fun getUserProfile(): Response<ProfileResponse>

  @GET("courses")
  suspend fun getCourses(): Response<List<CourseResponse>>

  @POST("users/refresh-token")
  suspend fun refreshToken(@Body refreshToken: String): Response<TokenResponse>

  @GET("posts")
  suspend fun getPosts(
    @Query("page") page: Int,
    @Query("pageSize") pageSize: Int
  ): Response<PostResponse>

  @POST("posts/create")
  suspend fun createPost(@Body post: PostRequest): Response<PostResponse>
}