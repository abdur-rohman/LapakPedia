package dev.rohman.lapakpedia.models

data class DemandModel(
    val id: Long,
    val productId: Long,
    val product: String,
    val email: String,
    val price: Double,
    val quantity: Int,
    val image: String,
    val address: String,
    val amount: Double,
    val weight: Int,
    val cost: Int,
    val etd: String
)