package com.example.project_x.data.model

data class PostResponse(
    val message: String?, // Posts retrieved successfully
    val posts: List<Post?>?
)