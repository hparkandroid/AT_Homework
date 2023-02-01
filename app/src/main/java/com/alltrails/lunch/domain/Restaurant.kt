package com.alltrails.lunch.domain

import com.alltrails.lunch.data.network.SearchResultResponse

data class Restaurant(
    val id: String,
    val name: String,
    val rating: Float?,
    val imageUrl: String?,
)

internal fun SearchResultResponse.mapToDomain(): Restaurant? {
    return Restaurant(
        name = name ?: return null,
        id = place_id ?: return null,
        rating = rating,
        imageUrl = icon,
    )
}