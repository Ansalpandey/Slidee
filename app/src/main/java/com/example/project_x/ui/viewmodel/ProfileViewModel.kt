package com.example.project_x.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.common.Resource
import com.example.project_x.data.model.ProfileResponse
import com.example.project_x.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
  ViewModel() {

  private val _profileState = MutableStateFlow<Resource<ProfileResponse>>(Resource.Loading())
  val userProfileState: StateFlow<Resource<ProfileResponse>> = _profileState.asStateFlow()

  init {
    fetchUserProfile()
  }

  fun fetchUserProfile() {
    viewModelScope.launch {
      userRepository.getUserProfile().collect { resource ->
        _profileState.value = resource
        Log.d("ProfileViewModel", "fetchUserProfile: $resource")
      }
    }
  }
}
