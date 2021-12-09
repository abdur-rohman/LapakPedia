package dev.rohman.lapakpedia.repos.remote.services

import dev.rohman.lapakpedia.repos.remote.requests.TokenRequest
import dev.rohman.lapakpedia.repos.remote.responses.BaseResponse
import dev.rohman.lapakpedia.repos.remote.responses.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserService {
    @PUT("v1/users/token")
    suspend fun updateNotificationToken(@Body request: TokenRequest): BaseResponse<UserResponse>

    @GET("v1/users/profile")
    suspend fun getProfile(): BaseResponse<UserResponse>

    @Multipart()
    @PUT("v1/users")
    suspend fun updateProfile(
        @PartMap body: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): BaseResponse<UserResponse>
}