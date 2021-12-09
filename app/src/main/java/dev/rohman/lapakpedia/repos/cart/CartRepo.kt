package dev.rohman.lapakpedia.repos.cart

import dev.rohman.lapakpedia.models.CartModel
import dev.rohman.lapakpedia.repos.local.daos.CartDao
import dev.rohman.lapakpedia.repos.local.entities.CartEntity
import dev.rohman.lapakpedia.repos.local.entities.SellerEntity
import dev.rohman.lapakpedia.repos.local.entities.toModel

class CartRepo(private val dao: CartDao) : CartRepository {
    override suspend fun insertOrderAndSeller(cart: CartEntity, seller: SellerEntity) =
        dao.addOrderAndSeller(cart, seller)

    override suspend fun getAllCart(email: String): List<CartModel> =
        dao.getAllCart(email).toModel()

    override suspend fun deleteCart(cart: CartEntity) = dao.deleteCart(cart)
}