package dev.rohman.lapakpedia.repos.remote.services

import dev.rohman.lapakpedia.repos.remote.requests.CostRequest
import dev.rohman.lapakpedia.repos.remote.responses.BaseRajaOngkirResponse
import dev.rohman.lapakpedia.repos.remote.responses.CityResponse
import dev.rohman.lapakpedia.repos.remote.responses.CostResponse
import dev.rohman.lapakpedia.repos.remote.responses.ProvinceResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RajaOngkirService {
    @GET("province")
    suspend fun getAllProvince(): BaseRajaOngkirResponse<ProvinceResponse>

    @GET("city")
    suspend fun getAllCity(): BaseRajaOngkirResponse<CityResponse>

    @POST("cost")
    suspend fun calculateCost(@Body request: CostRequest): BaseRajaOngkirResponse<CostResponse>
}