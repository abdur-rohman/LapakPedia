package dev.rohman.lapakpedia.repos.remote.responses

import dev.rohman.lapakpedia.models.CostItemModel

data class CostItemResponse(
    val note: String,
    val etd: String,
    val value: Int
)

fun CostItemResponse.toModel() = CostItemModel(note = note, etd = etd, value = value)
fun List<CostItemResponse>.toModel() = asSequence().map { it.toModel() }.toList()