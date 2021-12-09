package dev.rohman.lapakpedia.repos.remote.responses

import com.google.gson.annotations.SerializedName
import dev.rohman.lapakpedia.models.CityModel

data class CityResponse(
    @SerializedName("city_name") val cityName: String?,
    @SerializedName("province") val province: String?,
    @SerializedName("province_id") val provinceId: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("postal_code") val postalCode: String?,
    @SerializedName("city_id") val cityId: String?
)

fun CityResponse?.toModel() = CityModel(
    this?.cityName ?: "",
    this?.province ?: "",
    this?.provinceId ?: "",
    this?.type ?: "",
    this?.postalCode ?: "",
    this?.cityId ?: ""
)

fun List<CityResponse>?.toModel() = this?.asSequence()?.map { it.toModel() }?.toList() ?: listOf()