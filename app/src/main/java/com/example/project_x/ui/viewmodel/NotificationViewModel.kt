package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.project_x.data.model.NotificationResponse
import com.example.project_x.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class NotificationViewModel
@Inject
constructor(private val notificationRepository: NotificationRepository) : ViewModel() {
  private val _notifications = MutableStateFlow<List<NotificationResponse>>(emptyList())
  val notifications: StateFlow<List<NotificationResponse>> = _notifications.asStateFlow()

  suspend fun getNotifications(userId: String) {
    val notifications = notificationRepository.getNotifications("6708e65b2ba76fa5614827e4")
    _notifications.value = notifications
  }
}
