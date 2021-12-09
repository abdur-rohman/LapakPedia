package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.rohman.lapakpedia.databinding.FragmentCartsBinding
import dev.rohman.lapakpedia.models.CartModel
import dev.rohman.lapakpedia.repos.local.entities.toEntity
import dev.rohman.lapakpedia.utils.preferences.LocalStorage
import dev.rohman.lapakpedia.utils.showError
import dev.rohman.lapakpedia.viewmodels.CartsState
import dev.rohman.lapakpedia.viewmodels.CartsViewModel
import dev.rohman.lapakpedia.views.adapters.CartAdapter
import org.koin.android.ext.android.inject

class CartsFragment : Fragment(), CartAdapter.CartListener {

    private lateinit var binding: FragmentCartsBinding
    private val adapter by lazy { CartAdapter(requireContext(), this) }
    private val viewModel by inject<CartsViewModel>()
    private val localStorage by inject<LocalStorage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartsBinding.inflate(inflater, container, false).apply {
            rvCarts.adapter = adapter

            srlCart.setOnRefreshListener { viewModel.getAllCart(localStorage.email) }

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is CartsState.Loading -> showLoading(true)
                    is CartsState.SuccessDeleteCart -> {
                        showLoading(false)

                        adapter.deleteCart(it.data)
                    }
                    is CartsState.Error -> {
                        showLoading(false)

                        root.showError(it.exception.message)
                    }
                    is CartsState.SuccessCreateOrder -> {
                        showLoading(false)
                    }
                    is CartsState.SuccessGetAllCart -> {
                        showLoading(false)

                        adapter.list = it.data.toMutableList()
                    }
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllCart(localStorage.email)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.srlCart.isRefreshing = isLoading

        binding.btnPay.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.rvCarts.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDelete(cart: CartModel) {
        viewModel.deleteCart(cart.order.toEntity())
    }
}