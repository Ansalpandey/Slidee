package com.example.project_x.data.repository

import com.example.project_x.data.model.NotificationResponse

interface NotificationRepository {
    suspend fun getNotifications(userId: String): List<NotificationResponse>
}
