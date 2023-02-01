package com.alltrails.lunch.data.network

import kotlinx.serialization.Serializable

@Serializable
data class NearbySearchResponse(
    val results: List<SearchResultResponse>,
)

@Serializable
data class SearchResultResponse(
    val name: String?,
    val place_id: String?,
    val icon: String?,
    val rating: Float?,
)