package dev.rohman.lapakpedia.models

data class CityModel(
    val cityName: String,
    val province: String,
    val provinceId: String,
    val type: String,
    val postalCode: String,
    val cityId: String
)