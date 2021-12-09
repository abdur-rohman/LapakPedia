package dev.rohman.lapakpedia.repos.remote.responses

import com.google.gson.annotations.SerializedName

data class MidtransResponse(
    val token: String,
    @SerializedName("redirect_url") val redirectUrl: String
)