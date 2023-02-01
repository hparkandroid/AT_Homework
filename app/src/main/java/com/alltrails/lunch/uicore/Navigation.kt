@file:OptIn(ExperimentalAnimationApi::class)

package com.alltrails.lunch.uicore

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

fun AnimatedContentScope<NavBackStackEntry>.slideInLeft(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(300),
) = slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec)

fun AnimatedContentScope<NavBackStackEntry>.slideInRight(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(300),
) = slideIntoContainer(AnimatedContentScope.SlideDirection.Right, animationSpec)

fun AnimatedContentScope<NavBackStackEntry>.slideOutRight(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(300),
) = slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec)

fun AnimatedContentScope<NavBackStackEntry>.slideOutLeft(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(300),
) = slideOutOfContainer(AnimatedContentScope.SlideDirection.Left, animationSpec)