package dev.rohman.lapakpedia.repos.rajaongkir

import dev.rohman.lapakpedia.models.CityModel
import dev.rohman.lapakpedia.models.CostModel
import dev.rohman.lapakpedia.models.ProvinceModel
import dev.rohman.lapakpedia.repos.remote.requests.CostRequest
import dev.rohman.lapakpedia.repos.remote.responses.toModel
import dev.rohman.lapakpedia.repos.remote.services.RajaOngkirService

class RajaOngkirRepo(private val service: RajaOngkirService) : RajaOngkirRepository {
    override suspend fun getAllProvince(): List<ProvinceModel> =
        service.getAllProvince().rajaongkir?.results.toModel()

    override suspend fun getAllCity(): List<CityModel> =
        service.getAllCity().rajaongkir?.results.toModel()

    override suspend fun calculateCost(request: CostRequest): List<CostModel> =
        service.calculateCost(request = request).rajaongkir?.results.toModel()
}