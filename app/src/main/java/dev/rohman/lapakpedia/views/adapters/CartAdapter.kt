package dev.rohman.lapakpedia.views.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.databinding.ItemCartBinding
import dev.rohman.lapakpedia.models.CartModel
import dev.rohman.lapakpedia.utils.toLocalNumber
import dev.rohman.lapakpedia.utils.toRupiah

class CartAdapter(private val context: Context, private val listener: CartListener) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    interface CartListener {
        fun onDelete(cart: CartModel)
    }

    inner class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(cart: CartModel) {
            binding.run {
                ivProduct.load(cart.order.image) { error(R.drawable.ic_not_found) }
                ivSeller.load(cart.seller.image) {
                    transformations(CircleCropTransformation())
                    error(R.drawable.ic_not_found)
                }

                tvSeller.text = cart.seller.name
                tvQuantity.text = "Jumlah: ${cart.order.quantity}"
                tvDeliveringAddress.text = cart.seller.address
                tvShippingAddress.text = cart.order.address
                tvDeliveryCost.text = "Ongkir ${cart.order.cost.toRupiah()} dan pengiriman ${cart.order.etd} hari"
                tvWeight.text = "Berat barang: ${cart.order.weight.toLocalNumber()} g"
                tvTotal.text = "Total: ${cart.order.amount.toRupiah()}"
            }
        }
    }

    var list: MutableList<CartModel> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun deleteCart(id: Long) {
        val index = list.indexOfFirst { it.order.id == id }

        if (index != -1) {
            list.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCartBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = list[position]

        holder.bindData(cart)
        holder.binding.ivDelete.setOnClickListener { listener.onDelete(cart) }
    }

    override fun getItemCount(): Int = list.size
}