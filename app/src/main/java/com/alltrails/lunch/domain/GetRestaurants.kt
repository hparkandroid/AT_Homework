package com.alltrails.lunch.domain

import com.alltrails.lunch.ApplicationScope
import com.alltrails.lunch.data.GetRestaurantsResult
import com.alltrails.lunch.data.RestaurantsRepo
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

fun interface GetRestaurants {
    suspend operator fun invoke(
        keyword: String?,
        lat: Double,
        long: Double,
    ): GetRestaurantsResult
}

@ContributesBinding(ApplicationScope::class)
class RealGetRestaurants @Inject constructor(
    private val restaurantsRepo: RestaurantsRepo,
) : GetRestaurants {
    override suspend operator fun invoke(
        keyword: String?,
        lat: Double,
        long: Double,
    ): GetRestaurantsResult {
        // Usecase, if repo doesnt handle it locally and favorites are stored in another API call
        // could handle mutating the restaurants to add the favorites
        return restaurantsRepo.getRestaurants(keyword, lat, long)
    }
}

