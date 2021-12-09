package dev.rohman.lapakpedia.repos.auth

import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.remote.requests.LoginRequest
import dev.rohman.lapakpedia.repos.remote.requests.UserRequest

interface AuthRepository {
    suspend fun register(request: UserRequest): UserModel

    suspend fun login(request: LoginRequest): UserModel

    suspend fun logout(): UserModel
}