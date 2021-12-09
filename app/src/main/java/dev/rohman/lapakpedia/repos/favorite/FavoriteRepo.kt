package dev.rohman.lapakpedia.repos.favorite

import dev.rohman.lapakpedia.models.ProductModel
import dev.rohman.lapakpedia.repos.remote.responses.toModel
import dev.rohman.lapakpedia.repos.remote.services.FavoriteService

class FavoriteRepo(private val service: FavoriteService) : FavoriteRepository {
    override suspend fun getAllFavoriteProduct(): List<ProductModel> =
        service.getAllFavoriteProduct().data.toModel()

    override suspend fun favoriteProduct(id: Int): ProductModel =
        service.favoriteProduct(id).data.toModel()

    override suspend fun unFavoriteProduct(id: Int): ProductModel =
        service.unFavoriteProduct(id).data.toModel()
}