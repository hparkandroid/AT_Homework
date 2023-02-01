package com.alltrails.lunch.presentation.search

import com.alltrails.lunch.data.GetRestaurantsResult
import com.alltrails.lunch.domain.GetRestaurants
import com.alltrails.lunch.domain.Restaurant
import com.alltrails.lunch.presentation.search.SearchRestaurantsState.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRestaurantsViewModelTest {
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun `set location success`() = runTest {
        val viewModel = createViewModel(
            getRestaurants = { _, _, _ -> GetRestaurantsResult.Success(listOf(Fixtures.restaurant)) }
        )

        viewModel.setLocation(1.0, 2.0)

        assertEquals(
            SearchRestaurantsState(
                isLoading = false,
                location = Location(lat = 1.0, long = 2.0),
                restaurants = listOf(RestaurantUiModel("123", "Starbucks", "4.0", "imageUrl"))
            ),
            viewModel.state.value,
        )
    }

    @Test
    fun `set location error`() {
        val viewModel = createViewModel(
            getRestaurants = { _, _, _ -> GetRestaurantsResult.Error }
        )

        viewModel.setLocation(1.0, 2.0)

        assertEquals(
            SearchRestaurantsState(
                isLoading = false,
                location = Location(lat = 1.0, long = 2.0),
                error = SearchRestaurantsState.Error.Generic,
            ),
            viewModel.state.value,
        )
    }

    @Test
    fun setQuery() {
        // TODO write test
    }

    private fun createViewModel(
        getRestaurants: GetRestaurants,
    ) = SearchRestaurantsViewModel(
        getRestaurants = getRestaurants
    )

    private object Fixtures {
        val restaurant = Restaurant(
            id = "123",
            name = "Starbucks",
            rating = 4f,
            imageUrl = "imageUrl",
        )
    }
}

