package com.alltrails.lunch.presentation.search

import com.alltrails.lunch.ApplicationScope
import com.alltrails.lunch.domain.GetRestaurants
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component

// Can use dagger to provide fakes, etc. Too time consuming to set up for a sample project
// Pros and cons with this approach, however depending on the size and testing strategy, Dagger is helpful for test dependencies
//@MergeComponent(
//    scope = ApplicationScope::class,
//    exclude = [
//        GetRestaurants::class,
//    ]
//)
//interface TestComponent : SearchRestaurantsViewModel.Component {
//    @Component.Factory
//    interface Factory {
//        fun create(
//            @BindsInstance getRestaurants: GetRestaurants,
//        ): TestComponent
//    }
//
//    companion object : Factory by DaggerTestComponent.factory()
//}
