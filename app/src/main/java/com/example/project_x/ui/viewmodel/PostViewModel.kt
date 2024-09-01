package com.example.project_x.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PostViewModel
@Inject
constructor(
  private val postRepository: PostRepository,
  private val postPagingSource: PostPagingSource,
) : ViewModel() {
  private val _posts = MutableStateFlow<PagingData<Post>>(PagingData.empty())
  val posts: StateFlow<PagingData<Post>> = _posts.asStateFlow()

  private val _isPostsFetched = MutableStateFlow(false) // Flag to track if posts are fetched
  private val _likePost = MutableStateFlow<Resource<PostLikeResponse>>(Resource.Loading())

  private val _post = MutableStateFlow<Resource<PostResponse>>(Resource.Loading())
  val post: StateFlow<Resource<PostResponse>> = _post
  var isPostCreated by mutableStateOf(false)
    private set

  private val _postLikes = mutableStateMapOf<String, Boolean>()

  init {
    if (!_isPostsFetched.value) { // Check if posts are already fetched
      getPosts()
    }
  }

  fun getPosts() {
    viewModelScope.launch(Dispatchers.IO) {
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
        .collect {
          _posts.value = it
          _isPostsFetched.value = true // Set flag to true after fetching
        }
    }
  }

  fun createPost(postRequest: PostRequest) {
    viewModelScope.launch(Dispatchers.IO) {
      _post.value = Resource.Loading() // Set loading state
      postRepository.createPost(postRequest).collect { resource ->
        _post.value = resource
        // Notify that a new post was created
        if (resource is Resource.Success) {
          isPostCreated = true
          refreshPosts()
        }
      }
    }
  }

  fun resetPostCreationState() {
    isPostCreated = false
  }

  fun likePost(postId: String) {
    viewModelScope.launch(Dispatchers.IO) {
      postRepository.likePost(postId).collect { resource ->
        _likePost.value = resource
        if (resource is Resource.Success) {
          _postLikes[postId] = true
        }
      }
    }
  }

  fun unLikePost(postId: String) {
    viewModelScope.launch(Dispatchers.IO) {
      postRepository.unLikePost(postId).collect { resource ->
        _likePost.value = resource
        if (resource is Resource.Success) {
          _postLikes[postId] = false
        }
      }
    }
  }

  fun refreshPosts() {
    viewModelScope.launch(Dispatchers.IO) {
      // Clear existing posts
      _posts.value = PagingData.empty()
      _isPostsFetched.value = false // Reset the flag when refreshing posts
      // Re-fetch posts to include the new post at the top
      getPosts()
    }
  }

  fun isPostLiked(postId: String): Boolean {
    return _postLikes[postId] ?: false
  }
}
