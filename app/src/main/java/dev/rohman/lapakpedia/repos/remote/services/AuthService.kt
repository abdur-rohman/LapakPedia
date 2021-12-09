package dev.rohman.lapakpedia.repos.remote.services

import dev.rohman.lapakpedia.repos.remote.requests.LoginRequest
import dev.rohman.lapakpedia.repos.remote.requests.UserRequest
import dev.rohman.lapakpedia.repos.remote.responses.BaseResponse
import dev.rohman.lapakpedia.repos.remote.responses.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    @POST("v1/signup")
    suspend fun register(@Body request: UserRequest): BaseResponse<UserResponse>

    @POST("v1/signin")
    suspend fun login(@Body request: LoginRequest): BaseResponse<UserResponse>

    @GET("v1/signout")
    suspend fun logout(): BaseResponse<UserResponse>
}