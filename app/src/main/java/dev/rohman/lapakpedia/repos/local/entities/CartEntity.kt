package dev.rohman.lapakpedia.repos.local.entities

import androidx.room.*
import dev.rohman.lapakpedia.models.DemandModel

@Entity(tableName = "carts")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "product_id", defaultValue = "0") val productId: Long,
    @ColumnInfo(name = "product", defaultValue = "") val product: String,
    @ColumnInfo(name = "email", defaultValue = "") val email: String,
    @ColumnInfo(name = "price", defaultValue = "0.0") val price: Double,
    @ColumnInfo(name = "quantity", defaultValue = "0") val quantity: Int,
    @ColumnInfo(name = "image", defaultValue = "") val image: String,
    @ColumnInfo(name = "address", defaultValue = "") val address: String,
    @ColumnInfo(name = "amount", defaultValue = "0.0") val amount: Double,
    @ColumnInfo(name = "weight", defaultValue = "0") val weight: Int,
    @ColumnInfo(name = "cost", defaultValue = "0") val cost: Int,
    @ColumnInfo(name = "etd", defaultValue = "") val etd: String,
)

fun CartEntity.toModel() =
    DemandModel(id, productId, product, email, price, quantity, image, address, amount, weight, cost, etd)

fun DemandModel.toEntity() =
    CartEntity(id, productId, product, email, price, quantity, image, address, amount, weight, cost, etd)