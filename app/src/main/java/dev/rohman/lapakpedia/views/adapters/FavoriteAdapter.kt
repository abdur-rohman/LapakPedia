package dev.rohman.lapakpedia.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.rohman.lapakpedia.databinding.ItemFavoriteBinding
import dev.rohman.lapakpedia.models.ProductModel
import java.text.NumberFormat
import java.util.*

class FavoriteAdapter(
    private val context: Context, private val listener: FavoriteListener
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    interface FavoriteListener {
        fun onFavorite(product: ProductModel)
    }

    inner class ViewHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(product: ProductModel) {
            val numberFormat = NumberFormat.getNumberInstance(Locale("in", "ID"))

            binding.ivProduct.load(product.image)
            binding.tvPrice.text = "Rp. ${numberFormat.format(product.price)},00"
            binding.tvProduct.text = product.title
        }
    }

    var list = mutableListOf<ProductModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = list[position]

        holder.bindData(product)
        holder.binding.ivFavorite.setOnClickListener { listener.onFavorite(product) }
    }

    override fun getItemCount(): Int = list.size

    fun unFavoriteProduct(product: ProductModel) {
        val index = list.indexOfFirst { it.id == product.id }

        if (index != -1) {
            list.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}