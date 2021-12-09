package dev.rohman.lapakpedia.views.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import dev.rohman.lapakpedia.R
import dev.rohman.lapakpedia.databinding.ItemCategoryBinding
import dev.rohman.lapakpedia.models.CategoryModel

class CategoryAdapter(
    private val context: Context, private val listener: CategoryListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    interface CategoryListener {
        fun onSelected(category: CategoryModel)
    }

    var list = listOf<CategoryModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selected: Int = 1
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(category: CategoryModel) {
            binding.tvCategory.text = category.category
            binding.tvCategory.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                ContextCompat.getDrawable(context, category.icon),
                null
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = list[position]

        holder.bindData(category)
        holder.binding.tvCategory.setOnClickListener { listener.onSelected(category) }
        holder.binding.tvCategory.background = ContextCompat.getDrawable(
            context,
            if (selected == category.id) R.drawable.bg_selected_category else R.drawable.bg_category
        )
    }

    override fun getItemCount(): Int = list.size
}