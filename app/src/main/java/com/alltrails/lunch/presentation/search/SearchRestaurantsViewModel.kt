package com.alltrails.lunch.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alltrails.lunch.ApplicationScope
import com.alltrails.lunch.ComponentHolder
import com.alltrails.lunch.data.GetRestaurantsResult
import com.alltrails.lunch.domain.GetRestaurants
import com.alltrails.lunch.presentation.search.SearchRestaurantsState.Error
import com.alltrails.lunch.presentation.search.SearchRestaurantsState.Location
import com.squareup.anvil.annotations.ContributesTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchRestaurantsViewModel @Inject constructor(
    private val getRestaurants: GetRestaurants,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchRestaurantsState())
    val state: StateFlow<SearchRestaurantsState> = _state.asStateFlow()

    fun setLocation(lat: Double, long: Double) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    location = Location(lat = lat, long = long)
                )
            )

            val result = getRestaurants(
                keyword = _state.value.searchTerm,
                lat = lat,
                long = long,
            )

            result.updateState()
        }
    }

    fun setQuery(searchTerm: String) {
        viewModelScope.launch {
            _state.emit(
                _state.value.copy(
                    searchTerm = searchTerm,
                )
            )

            // should add debounce
            val location = _state.value.location
            if (location != null) {
                getRestaurants(
                    keyword = searchTerm,
                    lat = location.lat,
                    long = location.long,
                ).updateState()
            } else {
                _state.emit(
                    _state.value.copy(
                        isLoading = false,
                        error = Error.NoLocation,
                    )
                )
            }
        }
    }

    private suspend fun GetRestaurantsResult.updateState() {
        when (this) {
            GetRestaurantsResult.Error -> _state.emit(
                _state.value.copy(
                    isLoading = false,
                    error = Error.Generic,
                )
            )

            is GetRestaurantsResult.Success -> _state.emit(
                _state.value.copy(
                    isLoading = false,
                    error = null,
                    restaurants = restaurants.map {
                        RestaurantUiModel(
                            id = it.id,
                            name = it.name,
                            rating = it.rating?.toString(),
                            imageUrl = it.imageUrl,
                        )
                    }
                )
            )
        }
    }

    @ContributesTo(ApplicationScope::class)
    interface Component {
        fun searchRestaurantsViewModel(): SearchRestaurantsViewModel
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}

data class SearchRestaurantsState(
    val isLoading: Boolean = true,
    val searchTerm: String = "",
    val location: Location? = null,
    val restaurants: List<RestaurantUiModel> = emptyList(),
    val error: Error? = null,
) {
    data class Location(
        val lat: Double,
        val long: Double,
    )

    sealed interface Error {
        object NoLocation : Error
        object Generic : Error
    }
}

@Composable
internal fun searchRestaurantsViewModel(): SearchRestaurantsViewModel =
    LocalContext.current.let { context ->
        viewModel {
            ((context.applicationContext as ComponentHolder).component as SearchRestaurantsViewModel.Component)
                .searchRestaurantsViewModel()
        }
    }