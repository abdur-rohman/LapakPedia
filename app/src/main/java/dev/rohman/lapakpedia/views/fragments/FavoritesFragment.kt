package dev.rohman.lapakpedia.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.rohman.lapakpedia.databinding.FragmentFavoritesBinding
import dev.rohman.lapakpedia.models.ProductModel
import dev.rohman.lapakpedia.utils.showError
import dev.rohman.lapakpedia.viewmodels.FavoritesState
import dev.rohman.lapakpedia.viewmodels.FavoritesViewModel
import dev.rohman.lapakpedia.views.adapters.FavoriteAdapter
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment(), FavoriteAdapter.FavoriteListener {

    private lateinit var binding: FragmentFavoritesBinding
    private val adapter by lazy { FavoriteAdapter(requireContext(), this) }
    private val viewModel by inject<FavoritesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false).apply {
            rvProducts.adapter = adapter

            viewModel.state.observe(viewLifecycleOwner) {
                when (it) {
                    is FavoritesState.Loading -> showLoading(true)
                    is FavoritesState.SuccessGetAllFavoriteProduct -> {
                        showLoading(false)

                        adapter.list = it.data.toMutableList()
                    }
                    is FavoritesState.SuccessUnFavoriteProduct -> {
                        showLoading(false)

                        adapter.unFavoriteProduct(it.data)
                    }
                    is FavoritesState.Error -> {
                        showLoading(false)

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

        viewModel.getAllFavoriteProduct()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.rvProducts.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.pbLoading.visibility = if (!isLoading) View.GONE else View.VISIBLE
    }

    override fun onFavorite(product: ProductModel) {
        viewModel.unFavoriteProduct(product.id)
    }
}