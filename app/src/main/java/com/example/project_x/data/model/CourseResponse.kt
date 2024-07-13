package com.example.project_x.data.model

data class CourseResponse(
  val courses: List<Course>?,
  val currentPage: Int?, // 2
  val message: String?, // Courses retrieved successfully!
  val totalCount: Int?, // 15
  val totalPages: Int? // 3
)