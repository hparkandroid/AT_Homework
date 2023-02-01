package com.alltrails.lunch.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import coil.compose.AsyncImage
import com.alltrails.lunch.R
import com.alltrails.lunch.presentation.search.SearchRestaurantsState
import com.alltrails.lunch.presentation.search.SearchRestaurantsViewModel
import com.alltrails.lunch.presentation.search.searchRestaurantsViewModel
import com.alltrails.lunch.ui.RestaurantId
import com.alltrails.lunch.uicore.Background
import com.alltrails.lunch.uicore.Permissions
import com.alltrails.lunch.uicore.SearchField
import com.alltrails.lunch.uicore.slideInLeft
import com.alltrails.lunch.uicore.slideInRight
import com.alltrails.lunch.uicore.slideOutLeft
import com.alltrails.lunch.uicore.slideOutRight
import com.google.accompanist.navigation.animation.composable
import com.google.android.gms.location.LocationServices


@OptIn(ExperimentalAnimationApi::class)
internal fun NavGraphBuilder.searchRestaurants(
    navController: NavController,
) {
    composable(
        route = "search",
        enterTransition = { slideInLeft() },
        exitTransition = { slideOutLeft() },
        popEnterTransition = { slideInRight() },
        popExitTransition = { slideOutRight() },
    ) {
        Permissions(
            permissions = listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            onDismiss = {
                // TODO add screen when denying permissions
            }) {
            SearchRestaurants(
                viewModel = searchRestaurantsViewModel(),
                onNavigateToRestaurant = { restaurantId ->
                    // could provide navController abstraction to viewModel to call nav routes directly
                    // from viewmodel
//                    TODO add restaurant details screen
//                    navController.navigate("restaurant/${restaurantId.value}}")
                },
            )
        }
    }
}

@Composable
private fun SearchRestaurants(
    viewModel: SearchRestaurantsViewModel,
    onNavigateToRestaurant: (RestaurantId) -> Unit,
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsState()
    val error = state.error

    Column(
        modifier = Modifier.background(Background),
    ) {
        Box(
            Modifier
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            SearchField(
                modifier = Modifier.fillMaxWidth(),
                value = state.searchTerm,
                onValueChange = viewModel::setQuery,
                hint = stringResource(id = R.string.search),
            )
        }

        when {
            state.isLoading -> LoadingContent()

            error != null -> when (error) {
                SearchRestaurantsState.Error.Generic -> ErrorContent(message = stringResource(id = R.string.oopsSomethingWentWrong))
                SearchRestaurantsState.Error.NoLocation -> ErrorContent(message = stringResource(id = R.string.locationWasNotFound))
            }

            state.restaurants.isEmpty() -> EmptyContent()

            else -> Content(
                state = state,
                onNavigateToRestaurant = onNavigateToRestaurant,
            )
        }

        findLocation(context) { location ->
            viewModel.setLocation(
                lat = location.latitude,
                long = location.longitude
            )
        }
    }
}

@SuppressLint("MissingPermission") // suppressed since permissions is checked earlier
@Composable
private fun findLocation(context: Context, onLocationFound: (Location) -> Unit) {
    // modify this to location on request if necessary
    LaunchedEffect(Unit) {
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    onLocationFound(location)
                }
            }
    }
}

@Composable
private fun Content(
    state: SearchRestaurantsState,
    onNavigateToRestaurant: (RestaurantId) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(state.restaurants) { restaurant ->
            RestaurantRow(uiModel = restaurant) {
                onNavigateToRestaurant(RestaurantId(restaurant.id))
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(42.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(42.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            text = message,
        )
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(42.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            text = stringResource(id = R.string.noRestaurantsWereFound),
        )
    }
}
