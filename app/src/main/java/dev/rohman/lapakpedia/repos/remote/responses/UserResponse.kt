package dev.rohman.lapakpedia.repos.remote.responses

import dev.rohman.lapakpedia.models.UserModel

data class UserResponse(
    val lastName: String?,
    val image: String?,
    val role: String?,
    val imageName: String?,
    val postalCode: String?,
    val cityId: Int?,
    val provinceId: Int?,
    val token: String?,
    val firstName: String?,
    val cityName: String?,
    val phone: String?,
    val id: Int?,
    val provinceName: String?,
    val email: String?,
    val notificationToken: String?
)

fun UserResponse?.toModel(): UserModel = UserModel(
    lastName = this?.lastName ?: "",
    image = this?.image ?: "",
    role = this?.role ?: "",
    imageName = this?.imageName ?: "",
    postalCode = this?.postalCode ?: "",
    cityId = this?.cityId ?: 0,
    provinceId = this?.provinceId ?: 0,
    token = this?.token ?: "",
    firstName = this?.firstName ?: "",
    cityName = this?.cityName ?: "",
    phone = this?.phone ?: "",
    id = this?.id ?: 0,
    provinceName = this?.provinceName ?: "",
    email = this?.email ?: "",
    notificationToken = this?.notificationToken ?: ""
)
