package dev.rohman.lapakpedia.repos.remote.responses

import dev.rohman.lapakpedia.models.CostModel

data class CostResponse(
    val costs: List<CostsItemResponse>?,
    val code: String?,
    val name: String?
)

fun CostResponse?.toModel() = CostModel(
    costs = this?.costs?.asSequence()?.map { it.toModel() }?.toList() ?: listOf(),
    code = this?.code ?: "",
    name = this?.name ?: ""
)

fun List<CostResponse>?.toModel() = this?.asSequence()?.map { it.toModel() }?.toList() ?: listOf()
