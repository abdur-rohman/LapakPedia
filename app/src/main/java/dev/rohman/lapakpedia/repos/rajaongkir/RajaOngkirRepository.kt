package dev.rohman.lapakpedia.repos.rajaongkir

import dev.rohman.lapakpedia.models.CityModel
import dev.rohman.lapakpedia.models.CostModel
import dev.rohman.lapakpedia.models.ProvinceModel
import dev.rohman.lapakpedia.repos.remote.requests.CostRequest

interface RajaOngkirRepository {
    suspend fun getAllProvince(): List<ProvinceModel>
    suspend fun getAllCity(): List<CityModel>
    suspend fun calculateCost(request: CostRequest): List<CostModel>
}