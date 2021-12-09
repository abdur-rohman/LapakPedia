package dev.rohman.lapakpedia.repos.remote.responses

data class BaseRajaOngkirResponse<DATA>(val rajaongkir: RajaOngkirResponse<DATA>?)
data class RajaOngkirResponse<RESULT>(val results: List<RESULT>?)