package com.example.project_x.data.model

data class NotificationResponse(
    val __v: Int?,
    val _id: String?,
    val createdAt: String?,
    val link: String?,
    val message: String?,
    val read: Boolean?,
    val updatedAt: String?,
    val user: CreatedBy?, // This is the problematic field
    val type: String?
)

