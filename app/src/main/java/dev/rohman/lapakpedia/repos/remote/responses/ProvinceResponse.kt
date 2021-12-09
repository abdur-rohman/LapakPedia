package dev.rohman.lapakpedia.repos.remote.responses

import com.google.gson.annotations.SerializedName
import dev.rohman.lapakpedia.models.ProvinceModel

data class ProvinceResponse(
    @SerializedName("province") val province: String,
    @SerializedName("province_id") val provinceId: String
)

fun ProvinceResponse?.toModel() =
    ProvinceModel(province = this?.province ?: "", provinceId = this?.provinceId ?: "")

fun List<ProvinceResponse>?.toModel() =
    this?.asSequence()?.map { it.toModel() }?.toList() ?: listOf()
