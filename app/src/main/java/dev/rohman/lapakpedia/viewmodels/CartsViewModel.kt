package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.CartModel
import dev.rohman.lapakpedia.repos.cart.CartRepository
import dev.rohman.lapakpedia.repos.local.entities.CartEntity
import dev.rohman.lapakpedia.repos.order.OrderRepository
import dev.rohman.lapakpedia.repos.remote.requests.OrderRequest

sealed class CartsState {
    data class Loading(val message: String = "Loading...") : CartsState()
    data class SuccessCreateOrder(val token: String) : CartsState()
    data class SuccessGetAllCart(val data: List<CartModel>) : CartsState()
    data class SuccessDeleteCart(val data: Long) : CartsState()
    data class Error(val exception: Exception) : CartsState()
}

class CartsViewModel(private val orderRepository: OrderRepository, private val cartRepository: CartRepository) :
    BaseViewModel() {
    private val mutableState by lazy { MutableLiveData<CartsState>() }
    val state: LiveData<CartsState> get() = mutableState

    fun createOrder(request: OrderRequest) {
        coroutines { mutableState.postValue(CartsState.SuccessCreateOrder(orderRepository.createOrder(request))) }
    }

    fun getAllCart(email: String) {
        coroutines { mutableState.postValue(CartsState.SuccessGetAllCart(cartRepository.getAllCart(email))) }
    }

    fun deleteCart(cart: CartEntity) {
        coroutines {
            cartRepository.deleteCart(cart)

            mutableState.postValue(CartsState.SuccessDeleteCart(cart.id))
        }
    }

    private fun coroutines(run: suspend () -> Unit) {
        mutableState.value = CartsState.Loading()

        callCoroutines(run) { mutableState.postValue(CartsState.Error(it)) }
    }
}