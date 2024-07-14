package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.common.Resource
import com.example.project_x.data.model.PostResponse
import com.example.project_x.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {
    private val _posts = MutableStateFlow<Resource<List<PostResponse>>>(Resource.Loading())
    val posts: StateFlow<Resource<List<PostResponse>>> = _posts

    fun getPosts() {
        viewModelScope.launch {
            postRepository.getPosts().collect { resource -> _posts.value = resource }
        }
    }
}
