package dev.rohman.lapakpedia.repos.remote.responses

import dev.rohman.lapakpedia.models.ProductFavoritesModel

data class ProductFavoritesResponse(val id: Int?, val userId: Int?)

fun ProductFavoritesResponse?.toModel() = ProductFavoritesModel(this?.id ?: 0, this?.userId ?: 0)
fun List<ProductFavoritesResponse>?.toModel() =
    this?.asSequence()?.map { it.toModel() }?.toList() ?: listOf()