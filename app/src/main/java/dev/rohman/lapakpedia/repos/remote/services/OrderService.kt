package dev.rohman.lapakpedia.repos.remote.services

import dev.rohman.lapakpedia.repos.remote.requests.OrderRequest
import dev.rohman.lapakpedia.repos.remote.responses.BaseResponse
import dev.rohman.lapakpedia.repos.remote.responses.MidtransResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderService {
    @POST("v1/orders")
    suspend fun createOrder(@Body request: OrderRequest): BaseResponse<MidtransResponse>
}