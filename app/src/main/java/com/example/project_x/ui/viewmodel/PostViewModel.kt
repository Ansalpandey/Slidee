package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.common.Resource
import com.example.project_x.data.model.PostRequest
import com.example.project_x.data.model.PostResponse
import com.example.project_x.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
  private val _posts = MutableStateFlow<Resource<PostResponse>>(Resource.Loading())
  val posts: StateFlow<Resource<PostResponse>> = _posts

  private val _post = MutableStateFlow<Resource<PostResponse>>(Resource.Loading())
  val post: StateFlow<Resource<PostResponse>> = _post

  fun getPosts() {
    viewModelScope.launch {
      _posts.value = Resource.Loading()
      postRepository.getPosts(page = 1, pageSize = 10).collect { resource ->
        _posts.value = resource
      }
    }
  }

  fun createPost(postRequest: PostRequest) {
    viewModelScope.launch {
      _post.value = Resource.Loading() // Set loading state
      postRepository.createPost(postRequest).collect { resource -> _post.value = resource }
    }
  }
}
