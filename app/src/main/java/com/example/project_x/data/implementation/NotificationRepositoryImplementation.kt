package com.example.project_x.data.implementation

import com.example.project_x.data.datasource.NotificationDataSource
import com.example.project_x.data.model.NotificationResponse
import com.example.project_x.data.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImplementation
@Inject
constructor(private val notificationDataSource: NotificationDataSource) : NotificationRepository {
  override suspend fun getNotifications(userId: String): List<NotificationResponse> {
    return notificationDataSource.getNotifications(userId)
  }
}
