package dev.rohman.lapakpedia.models

data class UserModel(
    val lastName: String,
    val image: String,
    val role: String,
    val imageName: String,
    val postalCode: String,
    val cityId: Int,
    val provinceId: Int,
    val token: String,
    val firstName: String,
    val cityName: String,
    val phone: String,
    val id: Int,
    val provinceName: String,
    val email: String,
    val notificationToken: String
)
