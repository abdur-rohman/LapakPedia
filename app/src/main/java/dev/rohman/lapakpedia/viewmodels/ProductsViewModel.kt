package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.ProductModel
import dev.rohman.lapakpedia.repos.favorite.FavoriteRepository
import dev.rohman.lapakpedia.repos.product.ProductRepository

sealed class ProductsState {
    data class Loading(val message: String = "Loading...") : ProductsState()
    data class Error(val exception: Exception) : ProductsState()
    data class SuccessFavoriteProduct(val data: ProductModel) : ProductsState()
    data class SuccessUnFavoriteProduct(val data: ProductModel) : ProductsState()
    data class SuccessGetAllProduct(val data: List<ProductModel>) : ProductsState()
    data class SuccessGetFilteredProduct(val data: List<ProductModel>) : ProductsState()
}

class ProductsViewModel(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository
) : BaseViewModel() {

    private val mutableState by lazy { MutableLiveData<ProductsState>() }
    val state: LiveData<ProductsState> get() = mutableState
    private var products = listOf<ProductModel>()

    fun getAllProduct() {
        coroutines {
            mutableState.postValue(
                ProductsState.SuccessGetAllProduct(
                    productRepository.getAllProduct().also { products = it }
                )
            )
        }
    }

    fun favoriteProduct(id: Int) {
        coroutines {
            mutableState.postValue(
                ProductsState.SuccessFavoriteProduct(favoriteRepository.favoriteProduct(id))
            )
        }
    }

    fun unFavoriteProduct(id: Int) {
        coroutines {
            mutableState.postValue(
                ProductsState.SuccessUnFavoriteProduct(favoriteRepository.unFavoriteProduct(id))
            )
        }
    }

    fun getFilteredProductByCategory(category: String) {
        coroutines {
            if (category.equals("Semua", true)) {
                mutableState.postValue(ProductsState.SuccessGetFilteredProduct(products))
            } else {
                mutableState.postValue(
                    ProductsState.SuccessGetFilteredProduct(
                        products.asSequence().filter { it.category == category }.toList()
                    )
                )
            }
        }
    }

    private fun coroutines(withLoading: Boolean = true, run: suspend () -> Unit) {
        if (withLoading) mutableState.value = ProductsState.Loading()

        callCoroutines(run) { mutableState.postValue(ProductsState.Error(it)) }
    }
}