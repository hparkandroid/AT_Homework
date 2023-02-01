package com.alltrails.lunch.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {
    @GET("/maps/api/place/nearbysearch/json")
    suspend fun getPlaces(
        @Query("keyword") keyword: String,
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") type: String,
        @Query("key") key: String
    ): Response<NearbySearchResponse>
}
