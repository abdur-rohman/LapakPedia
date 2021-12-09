package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.ProductModel
import dev.rohman.lapakpedia.repos.favorite.FavoriteRepository

sealed class FavoritesState {
    data class Loading(val message: String = "Loading...") : FavoritesState()
    data class SuccessGetAllFavoriteProduct(val data: List<ProductModel>) : FavoritesState()
    data class SuccessUnFavoriteProduct(val data: ProductModel) : FavoritesState()
    data class Error(val exception: Exception) : FavoritesState()
}

class FavoritesViewModel(private val repository: FavoriteRepository) : BaseViewModel() {
    private val mutableState by lazy { MutableLiveData<FavoritesState>() }
    val state: LiveData<FavoritesState> get() = mutableState

    fun getAllFavoriteProduct() {
        coroutines {
            mutableState.postValue(FavoritesState.SuccessGetAllFavoriteProduct(repository.getAllFavoriteProduct()))
        }
    }

    fun unFavoriteProduct(id: Int) {
        coroutines {
            mutableState.postValue(
                FavoritesState.SuccessUnFavoriteProduct(repository.unFavoriteProduct(id))
            )
        }
    }

    private fun coroutines(run: suspend () -> Unit) {
        mutableState.value = FavoritesState.Loading()

        callCoroutines(run) { mutableState.postValue(FavoritesState.Error(it)) }
    }
}