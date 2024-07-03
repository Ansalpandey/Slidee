package com.example.project_x.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_x.common.Resource
import com.example.project_x.data.model.User
import com.example.project_x.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _userProfileState = MutableStateFlow<Resource<User>>(Resource.Loading())
    val userProfileState: StateFlow<Resource<User>> = _userProfileState

    fun fetchUserProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile()
                .collect { resource -> _userProfileState.value = resource }
        }
    }
}
