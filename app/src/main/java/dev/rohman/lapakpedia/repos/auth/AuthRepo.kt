package dev.rohman.lapakpedia.repos.auth

import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.remote.requests.LoginRequest
import dev.rohman.lapakpedia.repos.remote.requests.UserRequest
import dev.rohman.lapakpedia.repos.remote.responses.toModel
import dev.rohman.lapakpedia.repos.remote.services.AuthService

class AuthRepo(private val service: AuthService) : AuthRepository {

    override suspend fun register(request: UserRequest): UserModel =
        service.register(request).data.toModel()

    override suspend fun login(request: LoginRequest): UserModel =
        service.login(request).data.toModel()

    override suspend fun logout(): UserModel = service.logout().data.toModel()
}