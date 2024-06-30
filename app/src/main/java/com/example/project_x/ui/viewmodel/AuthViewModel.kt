package com.example.project_x.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.data.model.User
import com.example.project_x.data.repository.UserRepository
import com.example.project_x.ui.stateholder.UserStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
  val userStateHolder = mutableStateOf(UserStateHolder())

  fun registerUser(user: User) {
    viewModelScope.launch {
      try {
        userStateHolder.value = userStateHolder.value.copy(isLoading = true)
        val result = userRepository.registerUser(user)
        userStateHolder.value = userStateHolder.value.copy(isLoading = false, data = result)
      } catch (e: Exception) {
        userStateHolder.value = userStateHolder.value.copy(isLoading = false, error = e.message ?: "An error occurred")
      }
    }
  }
}
