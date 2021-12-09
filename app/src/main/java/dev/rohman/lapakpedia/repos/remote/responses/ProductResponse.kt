package dev.rohman.lapakpedia.repos.remote.responses

import dev.rohman.lapakpedia.models.ProductModel

data class ProductResponse(
    val seller: UserResponse?,
    val image: String?,
    val imageName: String?,
    val description: String?,
    val weight: Int?,
    val title: String?,
    val userId: Int?,
    val productImages: List<ProductImagesResponse>?,
    val price: Int?,
    val productFavorites: List<ProductFavoritesResponse>?,
    val id: Int?,
    val category: String?,
    val stock: Int?
)

fun ProductResponse.toModel(): ProductModel = ProductModel(
    productFavorites = productFavorites.toModel(),
    productImages = productImages.toModel(),
    imageName = imageName ?: "",
    image = image ?: "",
    id = id ?: 0,
    category = category ?: "",
    description = description ?: "",
    price = price ?: 0,
    seller = seller.toModel(),
    stock = stock ?: 0,
    title = title ?: "",
    userId = userId ?: 0,
    weight = weight ?: 0
)

fun List<ProductResponse>.toModel() = asSequence().map { it.toModel() }.toList()