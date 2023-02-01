package com.alltrails.lunch

import android.app.Application

class LunchApp : Application(), ComponentHolder {
    override val component = DaggerApplicationComponent.factory().create(
        context = this,
        baseUrl = "https://maps.googleapis.com",
        placesApiKey = BuildConfig.PLACES_API_KEY,
    )

    override fun onCreate() {
        super.onCreate()
    }
}
