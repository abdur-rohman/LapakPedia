package dev.rohman.lapakpedia.models

import androidx.annotation.DrawableRes

data class CategoryModel(val id: Int, val category: String, @DrawableRes val icon: Int)
