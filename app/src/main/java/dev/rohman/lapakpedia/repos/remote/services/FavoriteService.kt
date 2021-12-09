package dev.rohman.lapakpedia.repos.remote.services

import dev.rohman.lapakpedia.repos.remote.responses.BaseResponse
import dev.rohman.lapakpedia.repos.remote.responses.ProductResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface FavoriteService {
    @GET("v1/favorites")
    suspend fun getAllFavoriteProduct(): BaseResponse<List<ProductResponse>>

    @GET("v1/favorites/{id}")
    suspend fun favoriteProduct(@Path("id") id: Int): BaseResponse<ProductResponse>

    @DELETE("v1/favorites/{id}")
    suspend fun unFavoriteProduct(@Path("id") id: Int): BaseResponse<ProductResponse>
}