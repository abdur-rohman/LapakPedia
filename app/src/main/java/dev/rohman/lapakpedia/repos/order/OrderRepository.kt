package dev.rohman.lapakpedia.repos.order

import dev.rohman.lapakpedia.repos.remote.requests.OrderRequest

interface OrderRepository {
    suspend fun createOrder(request: OrderRequest): String
}