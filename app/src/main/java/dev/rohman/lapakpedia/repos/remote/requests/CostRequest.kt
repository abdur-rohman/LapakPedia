package dev.rohman.lapakpedia.repos.remote.requests

data class CostRequest(
    val courier: String,
    val origin: Int,
    val destination: Int,
    val weight: Int
)

