package com.example.project_x.data.model

data class PostRequest(
    val content: String,
    val imageUrl: String?,
    val videoUrl: String?,
    val videoUrlBase64: String?,
    val imageUrlBase64: List<String>,
)
