package com.example.project_x.data.repository

import com.example.project_x.common.Resource
import com.example.project_x.data.model.CourseResponse
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
  suspend fun getCourses(
    page: Int,
    pageSize: Int
  ): Flow<Resource<CourseResponse>>

  suspend fun getUserCourses() : Flow<Resource<CourseResponse>>
}
