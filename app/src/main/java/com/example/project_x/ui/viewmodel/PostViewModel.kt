package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.project_x.common.Resource
import com.example.project_x.data.model.Post
import com.example.project_x.data.model.PostLikeResponse
import com.example.project_x.data.model.PostRequest
import com.example.project_x.data.model.PostResponse
import com.example.project_x.data.pagination.PostPagingSource
import com.example.project_x.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel
@Inject
constructor(
  private val postRepository: PostRepository,
  private val postPagingSource: PostPagingSource,
) : ViewModel() {
  private val _posts = MutableStateFlow<PagingData<Post>>(PagingData.empty())
  val posts: StateFlow<PagingData<Post>> = _posts.asStateFlow()

  private val _likePost = MutableStateFlow<Resource<PostLikeResponse>>(Resource.Loading())

  private val _post = MutableStateFlow<Resource<PostResponse>>(Resource.Loading())
  val post: StateFlow<Resource<PostResponse>> = _post

  fun getPosts() {
    viewModelScope.launch {
      Pager(
          config =
            PagingConfig(
              pageSize = 30, // Larger page size to reduce the number of API calls
              enablePlaceholders = false,
              initialLoadSize = 30, // Load a larger initial page size
            )
        ) {
          postPagingSource
        }
        .flow
        .cachedIn(viewModelScope)
        .collect { _posts.value = it }
    }
  }

  fun createPost(postRequest: PostRequest) {
    viewModelScope.launch {
      _post.value = Resource.Loading() // Set loading state
      postRepository.createPost(postRequest).collect { resource -> _post.value = resource }
    }
  }

  fun likePost(postId: String) {
    viewModelScope.launch {
      postRepository.likePost(postId).collect { resource -> _likePost.value = resource }
    }
  }

  fun unLikePost(postId: String) {
    viewModelScope.launch {
      postRepository.unLikePost(postId).collect { resource -> _likePost.value = resource }
    }
  }
}
