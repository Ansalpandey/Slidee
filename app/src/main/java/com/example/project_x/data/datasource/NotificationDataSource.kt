package com.example.project_x.data.datasource

import com.example.project_x.data.api.AuthenticatedApiService
import com.example.project_x.data.model.NotificationResponse
import javax.inject.Inject

class NotificationDataSource @Inject constructor(private val apiService: AuthenticatedApiService) {
    suspend fun getNotifications(userId: String) : List<NotificationResponse> = apiService.getNotifications(userId)
}
