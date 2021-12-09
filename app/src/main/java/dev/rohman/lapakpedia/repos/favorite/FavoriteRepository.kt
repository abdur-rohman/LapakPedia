package dev.rohman.lapakpedia.repos.favorite

import dev.rohman.lapakpedia.models.ProductModel

interface FavoriteRepository {
    suspend fun getAllFavoriteProduct(): List<ProductModel>
    suspend fun favoriteProduct(id: Int): ProductModel
    suspend fun unFavoriteProduct(id: Int): ProductModel
}