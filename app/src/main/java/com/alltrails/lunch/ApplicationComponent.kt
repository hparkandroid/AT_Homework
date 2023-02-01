package com.alltrails.lunch

import android.content.Context
import com.alltrails.lunch.data.network.BaseUrl
import com.alltrails.lunch.data.network.PlacesApiKey
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@MergeComponent(scope = ApplicationScope::class)
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance @BaseUrl baseUrl: String,
            @BindsInstance @PlacesApiKey placesApiKey: String,
        ): ApplicationComponent
    }
}
