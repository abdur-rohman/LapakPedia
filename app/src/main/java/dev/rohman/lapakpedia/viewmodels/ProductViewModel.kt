package dev.rohman.lapakpedia.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.rohman.lapakpedia.models.*
import dev.rohman.lapakpedia.repos.cart.CartRepository
import dev.rohman.lapakpedia.repos.local.entities.CartEntity
import dev.rohman.lapakpedia.repos.local.entities.SellerEntity
import dev.rohman.lapakpedia.repos.product.ProductRepository
import dev.rohman.lapakpedia.repos.rajaongkir.RajaOngkirRepository
import dev.rohman.lapakpedia.repos.remote.requests.CostRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

sealed class ProductState {
    data class Error(val exception: Exception) : ProductState()
    data class Loading(val message: String = "Loading...") : ProductState()
    data class SuccessGetProductById(val data: ProductModel) : ProductState()
}

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val rajaOngkirRepository: RajaOngkirRepository,
    private val cartRepository: CartRepository
) : BaseViewModel() {

    private val mutableState by lazy { MutableLiveData<ProductState>() }
    val state: LiveData<ProductState> get() = mutableState

    private val mutableDeliveryTypes by lazy { MutableLiveData<List<CostModel>>() }
    val deliveryTypes: LiveData<List<CostModel>> = mutableDeliveryTypes

    private val mutableDeliveryServices by lazy { MutableLiveData<List<CostsItemModel>>() }
    val deliveryServices: LiveData<List<CostsItemModel>> = mutableDeliveryServices

    private val product by lazy { MutableLiveData<ProductModel>() }

    private val cityId by lazy { MutableLiveData(0) }
    val weight by lazy { MutableLiveData(0) }
    val amount by lazy { MutableLiveData(0) }
    val total by lazy { MutableLiveData(0) }
    val quantity by lazy { MutableLiveData(0) }

    val deliveryCost by lazy { MutableLiveData<CostItemModel>() }

    fun getProductById(id: Int) {
        mutableState.value = ProductState.Loading()

        callCoroutines({
            mutableState.postValue(
                ProductState.SuccessGetProductById(productRepository.getProductById(id).also {
                    cityId.postValue(it.seller.cityId)
                    product.postValue(it)
                })
            )
        }) { mutableState.postValue(ProductState.Error(it)) }
    }

    fun calculateCost(buyer: UserModel) {
        callCoroutines({
            coroutineScope {
                val request = CostRequest(
                    courier = "jne",
                    origin = cityId.value ?: 0,
                    destination = buyer.cityId,
                    weight = weight.value ?: 0
                )

                val deliveryTypes = mutableListOf<CostModel>()

                val jneJob = async(Dispatchers.Default) { rajaOngkirRepository.calculateCost(request) }
                val posJob = async(Dispatchers.Default) {
                    rajaOngkirRepository.calculateCost(request.copy(courier = "pos"))
                }
                val tikiJob = async(Dispatchers.Default) {
                    rajaOngkirRepository.calculateCost(request.copy(courier = "tiki"))
                }

                deliveryTypes.add(jneJob.await().first())
                deliveryTypes.add(posJob.await().first())
                deliveryTypes.add(tikiJob.await().first())

                mutableDeliveryTypes.postValue(deliveryTypes)
            }
        }) { mutableState.postValue(ProductState.Error(it)) }
    }

    fun insertOrderAndSeller(buyer: UserModel, onSuccess: () -> Unit) {
        callCoroutines({
            cartRepository.insertOrderAndSeller(
                CartEntity(
                    0L,
                    (product.value?.id?.toLong() ?: 0),
                    product.value?.title ?: "",
                    buyer.email,
                    product.value?.price?.toDouble() ?: 0.0,
                    (quantity.value ?: 1),
                    product.value?.image ?: "",
                    "${buyer.provinceName}, ${buyer.cityName} Kode Pos: ${buyer.postalCode}",
                    (total.value?.toDouble() ?: 0.0),
                    (weight.value ?: 0),
                    (deliveryCost.value?.value ?: 0),
                    (deliveryCost.value?.etd ?: "")
                ),
                SellerEntity(
                    0, 0,
                    "${product.value?.seller?.firstName} ${product.value?.seller?.lastName}",
                    product.value?.seller?.image ?: "",
                    "${product.value?.seller?.provinceName}, ${product.value?.seller?.cityName} Kode Pos: ${product.value?.seller?.postalCode}"
                )
            )

            withContext(Dispatchers.Main) { onSuccess.invoke() }
        }) {}
    }

    fun filterServiceByType(position: Int) {
        mutableDeliveryServices.postValue(mutableDeliveryTypes.value?.get(position)?.costs)
    }

    fun calculateDeliveryCost(position: Int, itemPosition: Int) {
        val cost = mutableDeliveryTypes.value?.get(itemPosition)?.costs?.get(position)?.cost?.first()
        val amount = amount.value?.plus(cost?.value ?: 0)

        deliveryCost.postValue(cost)
        total.postValue(amount)
    }

    fun setValue(qty: Int, product: ProductModel) {
        weight.value = qty.times(product.weight)
        amount.value = qty.times(product.price)
        quantity.value = qty
    }
}