package dev.rohman.lapakpedia.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.databinding.ItemProductBinding
import dev.rohman.lapakpedia.models.ProductModel
import dev.rohman.lapakpedia.utils.toRupiah

class ProductAdapter(
    private val context: Context, private val listener: ProductListener
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    interface ProductListener {
        fun onFavorite(product: ProductModel)
        fun onCart(product: ProductModel)
    }

    inner class ViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(product: ProductModel) {
            binding.ivProduct.load(product.image) { error(R.drawable.ic_not_found) }
            binding.ivSeller.load(product.seller.image) {
                transformations(CircleCropTransformation())
                error(R.drawable.ic_not_found)
            }

            binding.tvSeller.text = "${product.seller.firstName} ${product.seller.lastName}"
            binding.tvPrice.text = product.price.toRupiah()
            binding.tvProduct.text = product.title
            binding.ivFavorite.setImageResource(
                if (product.productFavorites.isNotEmpty()) R.drawable.ic_favorite else R.drawable.ic_un_favorite
            )
        }
    }

    var list = mutableListOf<ProductModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = list[position]

        holder.bindData(product)
        holder.binding.ivFavorite.setOnClickListener { listener.onFavorite(product) }
        holder.binding.ivCart.setOnClickListener { listener.onCart(product) }
    }

    override fun getItemCount(): Int = list.size

    fun onFavoriteProduct(product: ProductModel) {
        val index = list.indexOfFirst { it.id == product.id }

        if (index != -1) {
            list[index] = product
            notifyItemChanged(index)
        }
    }
}