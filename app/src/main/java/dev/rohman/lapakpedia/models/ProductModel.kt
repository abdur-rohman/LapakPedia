package dev.rohman.lapakpedia.models

data class ProductModel(
    val seller: UserModel,
    val image: String,
    val imageName: String,
    val description: String,
    val weight: Int,
    val title: String,
    val userId: Int,
    val productImages: List<ProductImagesModel>,
    val price: Int,
    val productFavorites: List<ProductFavoritesModel>,
    val id: Int,
    val category: String,
    val stock: Int
)