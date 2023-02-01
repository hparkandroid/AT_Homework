package com.alltrails.lunch.data

import android.util.Log
import com.alltrails.lunch.ApplicationScope
import com.alltrails.lunch.data.network.PlacesApi
import com.alltrails.lunch.data.network.PlacesApiKey
import com.alltrails.lunch.data.network.SearchResultResponse
import com.alltrails.lunch.domain.Restaurant
import com.alltrails.lunch.domain.mapToDomain
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface RestaurantsRepo {
    suspend fun getRestaurants(
        keyword: String?,
        lat: Double,
        long: Double,
    ): GetRestaurantsResult
}

@ContributesBinding(ApplicationScope::class)
class RealRestaurantsRepo @Inject constructor(
    private val placesApi: PlacesApi,
    @PlacesApiKey private val placesApiKey: String,
) : RestaurantsRepo {
    override suspend fun getRestaurants(
        keyword: String?,
        lat: Double,
        long: Double
    ): GetRestaurantsResult {
        // TODO implement db and show last cached results so no loading state is necessary and immediately display data
        // Also DB could hold favorites and could mutate data class here
        return runCatching {
            placesApi.getPlaces(
                keyword = keyword.orEmpty(),
                type = RESTAURANT_TYPE,
                location = "$lat,$long",
                radius = RADIUS,
                key = placesApiKey,
            )
        }.fold(
            onSuccess = { response ->
                if (response.isSuccessful) {
                    val restaurants = response.body()
                        ?.results
                        ?.mapNotNull(SearchResultResponse::mapToDomain)
                        .orEmpty()

                    GetRestaurantsResult.Success(restaurants)
                } else {
                    Log.e("HPARK", response.errorBody()?.string().orEmpty())
                    GetRestaurantsResult.Error
                }
            },
            onFailure = { t ->
                Log.e("HPARK", t.message.orEmpty())
                GetRestaurantsResult.Error
            }
        )
    }
}

// Should use a general result class
sealed interface GetRestaurantsResult {
    data class Success(val restaurants: List<Restaurant>) : GetRestaurantsResult

    // Could be more specific depending on how errors are surfaced to the user and how errors are logged
    object Error : GetRestaurantsResult
}

private const val RADIUS = "25000"
private const val RESTAURANT_TYPE = "restaurant"