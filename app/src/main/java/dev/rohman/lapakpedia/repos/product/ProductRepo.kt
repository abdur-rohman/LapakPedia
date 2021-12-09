package dev.rohman.lapakpedia.repos.product

import dev.rohman.lapakpedia.models.ProductModel
import dev.rohman.lapakpedia.repos.remote.responses.toModel
import dev.rohman.lapakpedia.repos.remote.services.ProductService

class ProductRepo(private val service: ProductService) : ProductRepository {
    override suspend fun getAllProduct(): List<ProductModel> =
        service.getAllProducts().data.toModel()

    override suspend fun getProductById(id: Int): ProductModel =
        service.getProductById(id).data.toModel()
}