package com.example.project_x.data.datasource

import com.example.project_x.common.Resource
import com.example.project_x.data.api.AuthenticatedApiService
import com.example.project_x.data.model.CommentCreateResponse
import com.example.project_x.data.model.CommentRequest
import com.example.project_x.data.model.CommentResponse
import com.example.project_x.data.model.PostLikeResponse
import com.example.project_x.data.model.PostRequest
import com.example.project_x.data.model.PostResponse
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class PostDataSource
@Inject
constructor(private val authenticatedApiService: AuthenticatedApiService) {

  suspend fun getPosts(page: Int, pageSize: Int): Response<PostResponse> {
    return authenticatedApiService.getPosts(page, pageSize)
  }

  suspend fun createPost(postRequest: PostRequest): Flow<Resource<PostResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.createPost(postRequest)
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to create post: Empty response body")) }
      } else {
        emit(Resource.Error("Error creating post: ${response.message()}"))
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun likeUserPost(postId: String): Flow<Resource<PostLikeResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.likePost(postId)
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to like post: Empty response body")) }
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun unLikePost(postId: String): Flow<Resource<PostLikeResponse>> = flow {
    emit(Resource.Loading())
    try {
      val response = authenticatedApiService.unLikePost(postId)
      if (response.isSuccessful) {
        response.body()?.let { emit(Resource.Success(it)) }
          ?: run { emit(Resource.Error("Failed to like post: Empty response body")) }
      }
    } catch (e: Exception) {
      emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
    }
  }

  suspend fun getUserPostsById(id: String, page: Int, pageSize: Int): Response<PostResponse> {
    return authenticatedApiService.getUserPostsById(id, page, pageSize)
  }

  suspend fun getComments(postId: String, page: Int, pageSize: Int): CommentResponse {
    return authenticatedApiService.getComments(postId, page, pageSize)
  }

  suspend fun addComment(postId: String, content: String): Flow<Resource<CommentCreateResponse>> =
    flow {
      emit(Resource.Loading())

      try {
        val response = authenticatedApiService.addComment(postId, CommentRequest(postId, content))
        if (response.isSuccessful) {
          response.body()?.let { emit(Resource.Success(it)) }
            ?: run { emit(Resource.Error("Failed to add comment: Empty response body")) }
        }
      } catch (e: Exception) {
        emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
      }
    }
}
