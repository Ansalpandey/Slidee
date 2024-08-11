package com.example.project_x.data.model

data class SearchResponse(
  val currentPage: Int?, // 1
  val message: String?, // Users retrieved successfully
  val totalPages: Int?, // 1
  val users: List<SearchUserResponse>?
)