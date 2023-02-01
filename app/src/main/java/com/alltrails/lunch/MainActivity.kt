package com.alltrails.lunch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.alltrails.lunch.ui.search.searchRestaurants
import com.alltrails.lunch.ui.theme.AllTrailsTheme
import com.alltrails.lunch.uicore.Background
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberAnimatedNavController()

            AllTrailsTheme {
                AnimatedNavHost(
                    navController = navController,
                    startDestination = "search",
                ) {
                    searchRestaurants(navController)
                }
            }
        }
    }
}
