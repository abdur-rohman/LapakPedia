package dev.rohman.lapakpedia.repos.user

import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.remote.requests.TokenRequest
import dev.rohman.lapakpedia.repos.remote.requests.UserRequest

interface UserRepository {
    suspend fun updateNotificationToken(request: TokenRequest): UserModel
    suspend fun getProfile(): UserModel
    suspend fun updateProfile(request: UserRequest, image: String): UserModel
}