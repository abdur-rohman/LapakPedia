package dev.rohman.lapakpedia.repos.remote.services

import dev.rohman.lapakpedia.repos.remote.responses.BaseResponse
import dev.rohman.lapakpedia.repos.remote.responses.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("v1/products")
    suspend fun getAllProducts(): BaseResponse<List<ProductResponse>>

    @GET("v1/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): BaseResponse<ProductResponse>
}