package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.midtrans.sdk.corekit.callback.CheckoutCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.corekit.models.snap.Token
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.databinding.FragmentProductsBinding
import dev.rohman.lapakpedia.models.CategoryModel
import dev.rohman.lapakpedia.models.ProductModel
import dev.rohman.lapakpedia.utils.ConstantUtil
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.utils.showError
import dev.rohman.lapakpedia.viewmodels.CartsState
import dev.rohman.lapakpedia.viewmodels.CartsViewModel
import dev.rohman.lapakpedia.viewmodels.ProductsState
import dev.rohman.lapakpedia.viewmodels.ProductsViewModel
import dev.rohman.lapakpedia.views.adapters.CategoryAdapter
import dev.rohman.lapakpedia.views.adapters.ProductAdapter
import org.koin.android.ext.android.inject
import java.util.*

class ProductsFragment : Fragment(), CategoryAdapter.CategoryListener, ProductAdapter.ProductListener {

    private lateinit var binding: FragmentProductsBinding
    private val localStorage by inject<LocalStorage>()
    private val viewModel by inject<ProductsViewModel>()
    private val orderViewModel by inject<CartsViewModel>()

    private val categories by lazy {
        listOf(
            CategoryModel(1, "Semua", R.drawable.ic_flame),
            CategoryModel(2, "Makanan", R.drawable.ic_food),
            CategoryModel(3, "Elektronik", R.drawable.ic_electronic),
            CategoryModel(4, "Pakaian", R.drawable.ic_clothing),
            CategoryModel(5, "Otomotif", R.drawable.ic_automotive)
        )
    }
    private val categoryAdapter by lazy { CategoryAdapter(requireContext(), this) }
    private val productAdapter by lazy { ProductAdapter(requireContext(), this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false).apply {
            SdkUIFlowBuilder.init()
                .setClientKey(ConstantUtil.MIDTRANS_CLIENT_KEY)
                .setMerchantBaseUrl("${ConstantUtil.LAPAK_PEDIA_BASE_URL}v1/")
                .setContext(requireContext())
                .enableLog(true)
                .buildSDK()

            rvCategories.adapter = categoryAdapter
            categoryAdapter.list = categories

            rvProducts.adapter = productAdapter

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is ProductsState.Loading -> showLoading(true)
                    is ProductsState.SuccessGetAllProduct -> {
                        showLoading(false)

                        productAdapter.list = it.data.toMutableList()
                    }
                    is ProductsState.SuccessFavoriteProduct -> {
                        showLoading(false)

                        productAdapter.onFavoriteProduct(it.data)
                    }
                    is ProductsState.SuccessUnFavoriteProduct -> {
                        showLoading(false)

                        productAdapter.onFavoriteProduct(it.data)
                    }
                    is ProductsState.SuccessGetFilteredProduct -> {
                        showLoading(false)

                        productAdapter.list = it.data.toMutableList()
                    }
                    is ProductsState.Error -> {
                        showLoading(false)

                        root.showError(it.exception.message)
                    }
                    else -> throw IllegalStateException("Unsupported state type")
                }
            }

            orderViewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is CartsState.SuccessCreateOrder -> {
                        MidtransSDK.getInstance().startPaymentUiFlow(requireContext(), it.token)
                    }
                    is CartsState.Error -> {
                        root.showError(it.exception.message)
                    }
                    else -> throw IllegalStateException("Unsupported state type")
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllProduct()
        categoryAdapter.selected = categories.first().id
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvProducts.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.pbLoading.visibility = if (!isLoading) View.GONE else View.VISIBLE
    }

    private fun createOrder() {
        val transaction = TransactionRequest("SID-${Date().time}", 2000000.0).apply {
            customerDetails = CustomerDetails(
                localStorage.user.firstName,
                localStorage.user.lastName,
                localStorage.user.email,
                localStorage.user.phone
            ).apply {
                billingAddress = BillingAddress().apply {
                    address =
                        "${localStorage.cityName} ${localStorage.provinceName} ${localStorage.postalCode}"
                    city = localStorage.cityName
                    postalCode = localStorage.postalCode
                }

                shippingAddress = ShippingAddress().apply {
                    address =
                        "${localStorage.cityName} ${localStorage.provinceName} ${localStorage.postalCode}"
                    city = localStorage.cityName
                    postalCode = localStorage.postalCode
                }
            }

            itemDetails = arrayListOf(
                ItemDetails("1", 100000.0, 10, "Java"),
                ItemDetails("3", 100000.0, 10, "Kotlin")
            )

            customField1 = localStorage.user.email
        }

        MidtransSDK.getInstance().run {
            transactionRequest = transaction

            checkout(object : CheckoutCallback {
                override fun onError(t: Throwable?) {
                    binding.root.showError(t?.message)
                }

                override fun onSuccess(it: Token?) {
                    MidtransSDK.getInstance().startPaymentUiFlow(requireContext(), it?.tokenId)
                }

                override fun onFailure(token: Token?, message: String?) {
                    binding.root.showError(message)
                }
            })
        }
    }

    override fun onSelected(category: CategoryModel) {
        categoryAdapter.selected = category.id

        viewModel.getFilteredProductByCategory(category.category)
    }

    override fun onFavorite(product: ProductModel) {
        if (product.productFavorites.isEmpty()) viewModel.favoriteProduct(product.id)
        else viewModel.unFavoriteProduct(product.id)
    }

    override fun onCart(product: ProductModel) {
        val action = ProductsFragmentDirections.actionProductsFragmentToProductFragment(product.id)
        findNavController().navigate(action)
    }
}