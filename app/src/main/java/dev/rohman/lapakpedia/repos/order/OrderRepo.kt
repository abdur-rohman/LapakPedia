package dev.rohman.lapakpedia.repos.order

import dev.rohman.lapakpedia.repos.remote.requests.OrderRequest
import dev.rohman.lapakpedia.repos.remote.services.OrderService

class OrderRepo(private val service: OrderService) : OrderRepository {
    override suspend fun createOrder(request: OrderRequest): String =
        service.createOrder(request).data.token
}