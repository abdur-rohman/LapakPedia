package dev.rohman.lapakpedia.repos.product

import dev.rohman.lapakpedia.models.ProductModel

interface ProductRepository {
    suspend fun getAllProduct(): List<ProductModel>
    suspend fun getProductById(id: Int): ProductModel
}