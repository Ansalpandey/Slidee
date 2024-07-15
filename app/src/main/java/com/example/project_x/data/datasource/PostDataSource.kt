package com.example.project_x.data.datasource

import com.example.project_x.common.Resource
import com.example.project_x.data.api.AuthenticatedApiService
import com.example.project_x.data.model.PostRequest
import com.example.project_x.data.model.PostResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PostDataSource
@Inject
constructor(private val authenticatedApiService: AuthenticatedApiService) {
  suspend fun getPosts(): Flow<Resource<List<PostResponse>>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.getPosts()
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to fetch courses: Empty response body")) }
      } else {
        emit(Resource.Error("Error fetching courses: ${response.message()}"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun createPost(postRequest: PostRequest): Flow<Resource<PostResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.createPost(postRequest)
      if (response.isSuccessful) {
        val userResponse = response.body()
        emit(Resource.Success(userResponse))
      } else {
        emit(Resource.Error("Post Creation Failed failed"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }
}
