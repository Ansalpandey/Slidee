package com.example.project_x.ui.navigation

import kotlinx.serialization.Serializable

sealed class Route {

    @Serializable
    data object LoginScreen : Route()

    @Serializable
    data object RegisterScreen : Route()

    @Serializable
    data object HomeScreen : Route()

    @Serializable
    data object CreatePostScreen : Route()

    @Serializable
    data object ProfileScreen : Route()

    @Serializable
    data class UserProfileScreen(val userId: String) : Route()

    @Serializable
    data object SettingsScreen : Route()
}
