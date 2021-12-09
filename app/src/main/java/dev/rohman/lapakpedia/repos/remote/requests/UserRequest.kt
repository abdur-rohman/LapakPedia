package dev.rohman.lapakpedia.repos.remote.requests

import dev.rohman.lapakpedia.utils.toRequestBody
import okhttp3.RequestBody

data class UserRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val cityId: Int,
    val provinceId: Int,
    val cityName: String,
    val provinceName: String,
    val postalCode: String,
)

fun UserRequest.toFormData(): HashMap<String, RequestBody> = hashMapOf(
    "firstName" to firstName.toRequestBody(),
    "lastName" to lastName.toRequestBody(),
    "phone" to phone.toRequestBody(),
    "email" to email.toRequestBody(),
    "cityId" to cityId.toString().toRequestBody(),
    "provinceId" to provinceId.toString().toRequestBody(),
    "cityName" to cityName.toRequestBody(),
    "provinceName" to provinceName.toRequestBody(),
    "postalCode" to postalCode.toRequestBody()
)
