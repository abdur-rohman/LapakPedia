package dev.rohman.lapakpedia.repos.cart

import dev.rohman.lapakpedia.models.CartModel
import dev.rohman.lapakpedia.repos.local.entities.CartEntity
import dev.rohman.lapakpedia.repos.local.entities.SellerEntity

interface CartRepository {
    suspend fun insertOrderAndSeller(cart: CartEntity, seller: SellerEntity)
    suspend fun getAllCart(email: String): List<CartModel>
    suspend fun deleteCart(cart: CartEntity)
}