package dev.rohman.lapakpedia.repos.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import dev.rohman.lapakpedia.models.CartModel

data class CartAndSeller(
    @Embedded val cart: CartEntity,
    @Relation(parentColumn = "id", entityColumn = "order_id") val seller: SellerEntity
)

fun CartAndSeller.toModel() = CartModel(cart.toModel(), seller.toModel())
fun List<CartAndSeller>.toModel() = asSequence().map { it.toModel() }.toList()