package dev.rohman.lapakpedia.repos.remote.responses

import dev.rohman.lapakpedia.models.ProductImagesModel

data class ProductImagesResponse(
    val imageName: String?,
    val productId: Int?,
    val name: String?,
    val id: Int?
)

fun ProductImagesResponse.toModel() =
    ProductImagesModel(imageName ?: "", productId ?: 0, name ?: "", id ?: 0)

fun List<ProductImagesResponse>?.toModel() =
    this?.asSequence()?.map { it.toModel() }?.toList() ?: listOf()
