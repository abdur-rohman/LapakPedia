package dev.rohman.lapakpedia.repos.remote.responses

import dev.rohman.lapakpedia.models.CostsItemModel

data class CostsItemResponse(
    val cost: List<CostItemResponse>,
    val service: String,
    val description: String
)

fun CostsItemResponse.toModel() = CostsItemModel(
    cost = cost.asSequence().map { it.toModel() }.toList(),
    service = service,
    description = description
)

fun List<CostsItemResponse>.toModel() = asSequence().map { it.toModel() }.toList()