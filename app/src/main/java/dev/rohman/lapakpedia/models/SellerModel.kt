package dev.rohman.lapakpedia.models

data class SellerModel(
    val id: Long,
    val orderId: Long,
    val name: String,
    val image: String,
    val address: String,
)
