package dev.rohman.lapakpedia.repos.user

import dev.rohman.lapakpedia.models.UserModel
import dev.rohman.lapakpedia.repos.remote.requests.TokenRequest
import dev.rohman.lapakpedia.repos.remote.requests.UserRequest
import dev.rohman.lapakpedia.repos.remote.requests.toFormData
import dev.rohman.lapakpedia.repos.remote.responses.toModel
import dev.rohman.lapakpedia.repos.remote.services.UserService
import dev.rohman.lapakpedia.utils.toMultiPartBody

class UserRepo(private val service: UserService) : UserRepository {
    override suspend fun updateNotificationToken(request: TokenRequest): UserModel =
        service.updateNotificationToken(request).data.toModel()

    override suspend fun getProfile(): UserModel = service.getProfile().data.toModel()

    override suspend fun updateProfile(request: UserRequest, image: String): UserModel =
        service.updateProfile(request.toFormData(), image.toMultiPartBody()).data.toModel()
}