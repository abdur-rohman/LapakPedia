package dev.rohman.lapakpedia.models

data class CostModel(
    val costs: List<CostsItemModel>,
    val code: String,
    val name: String
)

data class CostsItemModel(
    val cost: List<CostItemModel>,
    val service: String,
    val description: String
)

data class CostItemModel(
    val note: String,
    val etd: String,
    val value: Int
)