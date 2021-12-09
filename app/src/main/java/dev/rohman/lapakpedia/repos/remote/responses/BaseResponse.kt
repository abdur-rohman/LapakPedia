package dev.rohman.lapakpedia.repos.remote.responses

data class BaseResponse<DATA>(val status: Boolean, val message: String, val data: DATA)
