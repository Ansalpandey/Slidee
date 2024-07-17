package com.example.project_x.data.repository

import com.example.project_x.common.Resource
import com.example.project_x.data.model.PostRequest
import com.example.project_x.data.model.PostResponse
import kotlinx.coroutines.flow.Flow

interface PostRepository {
  suspend fun getPosts(
    page: Int,
    pageSize: Int
  ): Flow<Resource<PostResponse>>

  suspend fun createPost(postRequest: PostRequest): Flow<Resource<PostResponse>>
}