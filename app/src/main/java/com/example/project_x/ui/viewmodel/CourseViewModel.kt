package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.common.Resource
import com.example.project_x.data.model.CourseResponse
import com.example.project_x.data.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(private val courseRepository: CourseRepository) :
  ViewModel() {
  private val _courses = MutableStateFlow<Resource<CourseResponse>>(Resource.Loading())
  val courses: StateFlow<Resource<CourseResponse>> = _courses

  private val _userCourses = MutableStateFlow<Resource<CourseResponse>>(Resource.Loading())
  val userCourses: StateFlow<Resource<CourseResponse>> = _userCourses

  fun getCourses() {
    viewModelScope.launch {
      courseRepository.getCourses(1, 10).collect { resource -> _courses.value = resource }
    }
  }

  fun getUserCourses() {
    viewModelScope.launch {
      courseRepository.getUserCourses().collect { resource -> _userCourses.value = resource }
    }
  }
}
