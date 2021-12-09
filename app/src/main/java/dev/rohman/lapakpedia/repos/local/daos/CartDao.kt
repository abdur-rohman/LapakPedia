package dev.rohman.lapakpedia.repos.local.daos

import androidx.room.*
import dev.rohman.lapakpedia.repos.local.entities.CartAndSeller
import dev.rohman.lapakpedia.repos.local.entities.CartEntity
import dev.rohman.lapakpedia.repos.local.entities.SellerEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM carts WHERE email=:email")
    suspend fun getAllCart(email: String): List<CartAndSeller>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCart(cart: CartEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSeller(seller: SellerEntity)

    @Delete
    suspend fun deleteCart(cart: CartEntity)

    @Transaction
    suspend fun addOrderAndSeller(cart: CartEntity, seller: SellerEntity) {
        val orderId = addCart(cart)

        if (orderId > 0) addSeller(seller.copy(orderId = orderId))
    }
}