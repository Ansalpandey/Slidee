package com.example.project_x.data.implementation

import android.util.Log
import com.example.project_x.common.Resource
import com.example.project_x.data.datasource.PostDataSource
import com.example.project_x.data.model.CommentCreateResponse
import com.example.project_x.data.model.CommentResponse
import com.example.project_x.data.model.PostLikeResponse
import com.example.project_x.data.model.PostRequest
import com.example.project_x.data.model.PostResponse
import com.example.project_x.data.repository.PostRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class PostRepositoryImplementation @Inject constructor(private val postDataSource: PostDataSource) :
  PostRepository {
  override suspend fun getPosts(page: Int, pageSize: Int): Response<PostResponse> {
    return postDataSource.getPosts(page, pageSize)
  }

  override suspend fun createPost(postRequest: PostRequest): Flow<Resource<PostResponse>> {
    return postDataSource.createPost(postRequest)
  }

  override suspend fun likePost(postId: String): Flow<Resource<PostLikeResponse>> {
    return postDataSource.likeUserPost(postId)
  }

  override suspend fun unLikePost(postId: String): Flow<Resource<PostLikeResponse>> {
    return postDataSource.unLikePost(postId)
  }

  override suspend fun getUsersPostById(
    id: String,
    page: Int,
    pageSize: Int,
  ): Response<PostResponse> {
    Log.d("PostRepository", "Fetching posts for user $id, page: $page, pageSize: $pageSize")
    val response = postDataSource.getUserPostsById(id, page, pageSize)
    Log.d("PostRepository", "Response posts: ${response.body()?.posts?.size ?: 0}")
    return response
  }

  override suspend fun getComments(postId: String, page: Int, pageSize: Int): CommentResponse {
    return postDataSource.getComments(postId, page, pageSize)
  }

  override suspend fun addComment(
    postId: String,
    content: String,
  ): Flow<Resource<CommentCreateResponse>> {
    return postDataSource.addComment(postId, content)
  }
}
