package yelp.yelp.com.yelp.interfaces;

import retrofit2.Call
import retrofit2.http.GET;
import retrofit2.http.Header
import retrofit2.http.Query;
import yelp.yelp.com.yelp.models.SearchResult

interface YelpSearchAPIService {
    @GET("businesses/search")
    fun searchBusiness(
        @Header("Authorization") authorization: String,
        @Query("latitude")latitude: Double,
        @Query("longitude")longitude: Double,
        @Query("term")term: String): Call<SearchResult>

}
