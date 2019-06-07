package com.chethan.abn.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Chethan on 5/3/2019.
 * This interface contains the definition list of all the network endpoints used by the App.
 * Ref: Retrofit
 */
interface NetWorkApi {

    @GET("v2/venues/{venueId}")
    fun getVenueDetails(
        @Path("venueId") venueId: String,
        @Query("client_id") appId: String,
        @Query("client_secret") appKey: String,
        @Query("v") v: String
    ): LiveData<ApiResponse<VenueDetailsResponse>>


    @GET("v2/venues/search")
    fun searchVenues(
        @Query("client_id") appId: String,
        @Query("client_secret") appKey: String,
        @Query("near") givenString: String,
        @Query("radius") radius: Int,
        @Query("v") v: String
    ): LiveData<ApiResponse<VenueSearchResponse>>
}