package dev.rohman.lapakpedia.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.databinding.DialogDeliveryCostBinding
import dev.rohman.lapakpedia.databinding.FragmentProductBinding
import dev.rohman.lapakpedia.models.ProductModel
import dev.rohman.lapakpedia.utils.*
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.viewmodels.ProductState
import dev.rohman.lapakpedia.viewmodels.ProductViewModel
import dev.rohman.lapakpedia.views.adapters.ImagePagerAdapter
import org.koin.android.ext.android.inject

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val args by navArgs<ProductFragmentArgs>()
    private val viewModel by inject<ProductViewModel>()
    private val localStorage by inject<LocalStorage>()
    private val adapter by lazy { ImagePagerAdapter(childFragmentManager, lifecycle) }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false).apply {
            srlProduct.setOnRefreshListener { viewModel.getProductById(args.id) }

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is ProductState.Loading -> showLoading(true)
                    is ProductState.Error -> {
                        showLoading(false)

                        root.showError(it.exception.message)
                    }
                    is ProductState.SuccessGetProductById -> {
                        showLoading(false)

                        val fragments = listOf(
                            ImageFragment.newInstance(it.data.image),
                            *it.data.productImages
                                .map { image -> ImageFragment.newInstance(image.name) }
                                .toTypedArray()
                        )

                        adapter.fragments = fragments

                        vpImages.adapter = adapter
                        ciDots.setViewPager(vpImages)

                        ciDots.visibility = if (it.data.productImages.isNotEmpty()) View.VISIBLE else View.GONE

                        ivSeller.load(it.data.seller.image) { transformations(CircleCropTransformation()) }

                        tvSeller.text = "${it.data.seller.firstName} ${it.data.seller.lastName}"
                        tvProduct.text = it.data.title
                        tvDescription.text = it.data.description
                        tvStock.text = "Stok: ${it.data.stock}"
                        tvPrice.text = it.data.price.toRupiah()
                        tvGrossAmount.text = it.data.price.toRupiah()

                        ivFavorite.setImageResource(
                            if (it.data.productFavorites.isNotEmpty()) R.drawable.ic_favorite
                            else R.drawable.ic_un_favorite
                        )

                        viewModel.quantity.value = 1
                        viewModel.weight.value = it.data.weight
                        viewModel.amount.value = it.data.price

                        etQuantity.doOnTextChanged { text, _, _, _ ->
                            val quantity = (text.toString().toIntOrNull() ?: 1)

                            if (quantity > it.data.stock) {
                                viewModel.setValue(it.data.stock, it.data)

                                tvGrossAmount.text = it.data.stock.times(it.data.price).toRupiah()
                                etQuantity.setText(it.data.stock.toString())
                                etQuantity.setSelection(it.data.stock.toString().length)
                            } else {
                                viewModel.setValue(quantity, it.data)

                                tvGrossAmount.text = quantity.times(it.data.price).toRupiah()
                            }
                        }

                        ibDecrease.setOnClickListener { _ -> updatePrice(false, it.data) }
                        ibIncrease.setOnClickListener { _ -> updatePrice(true, it.data) }
                    }
                }
            }

            btnAddToCart.setOnClickListener {
                if (etQuantity.text.toString().toInt() > 0) {
                    viewModel.calculateCost(localStorage.user)

                    val alertBuilder = AlertDialog.Builder(requireContext())

                    val binding = DialogDeliveryCostBinding.inflate(inflater).apply {
                        viewModel.deliveryTypes.observe(viewLifecycleOwner) { costs ->
                            pbLoading.visibility = View.GONE
                            clMain.visibility = View.VISIBLE

                            spDeliveryType.adapter = requireContext().getSpinnerAdapter(
                                costs.map { type -> type.name }
                            )
                        }

                        viewModel.deliveryServices.observe(viewLifecycleOwner) { services ->
                            spDeliveryService.adapter = requireContext().getSpinnerAdapter(
                                services.map { service -> service.service }
                            )
                        }

                        viewModel.deliveryCost.observe(viewLifecycleOwner) { cost ->
                            tvDeliveryCost.text = "Ongkir ${cost.value.toRupiah()} dan pengiriman ${cost.etd} hari"
                        }

                        viewModel.weight.observe(viewLifecycleOwner) { weight ->
                            tvWeight.text = "Berat barang: ${weight.toLocalNumber()} g"
                        }

                        viewModel.total.observe(viewLifecycleOwner) { total ->
                            tvAmount.text = total.toRupiah()
                        }

                        spDeliveryType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                viewModel.filterServiceByType(position)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }

                        spDeliveryService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                viewModel.calculateDeliveryCost(position, spDeliveryType.selectedItemPosition)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    }

                    alertBuilder.setTitle("Pilih ongkir yang akan digunakan")
                    alertBuilder.setView(binding.root)
                    alertBuilder.setPositiveButton("Tambahkan ke keranjang") { dialog, _ ->
                        viewModel.insertOrderAndSeller(localStorage.user) { dialog.dismiss() }
                    }
                    alertBuilder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }

                    alertBuilder.create().show()
                }
            }
        }

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.srlProduct.isRefreshing = isLoading
        binding.clProduct.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun updatePrice(isPositive: Boolean, product: ProductModel) {
        val qty = binding.etQuantity.text.toString().toInt()
        val quantity: String = if (isPositive) {
            if (qty < product.stock) (qty + 1).toString()
            else product.stock.toString()
        } else {
            if (qty > 1) (qty - 1).toString()
            else 1.toString()
        }

        viewModel.setValue(quantity.toInt(), product)

        binding.etQuantity.setText(quantity)
        binding.tvGrossAmount.text = quantity.toInt().times(product.price).toRupiah()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getProductById(args.id)
    }
}