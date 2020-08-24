package yelp.yelp.com.yelp.interfaces;

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET;
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query;
import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.models.SearchResult

interface YelpBusinessAPIService {
    @GET("businesses/{id}")
    fun getBusiness(
        @Header("Authorization") authorization: String,
        @Path(value="id", encoded = true)id: String) : Call<Business>

}
