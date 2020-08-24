package yelp.yelp.com.yelp.repositories

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelp.yelp.com.yelp.interfaces.YelpSearchAPIService
import yelp.yelp.com.yelp.models.SearchResult

object SearchResultRepository {

    // can be use for sqlite or api callback but we dont have sqlite for now
    interface SearchResultCallback {

        fun onResponseSuccess(searchResult: SearchResult?)
        fun onResponseFailure(error: String)
        fun onFailure(t: Throwable)

    }

    fun getSearchResult(auth: String, latitude: Double, longitude: Double, term: String, callback: SearchResultCallback): MutableLiveData<SearchResult> {

        var searchResult: MutableLiveData<SearchResult> = MutableLiveData()

        val request = ServiceBuilder.buildService(YelpSearchAPIService::class.java)

        val call = request.searchBusiness(auth, latitude, longitude, term)

        call.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {

                if ( response.isSuccessful ) {

                    callback.onResponseSuccess(response.body())
                } else {

                    callback.onResponseFailure(response.message())
                }


            }
            override fun onFailure(call: Call<SearchResult>, t: Throwable) {

                callback.onFailure(t)

            }

        })

        return searchResult;

    }

}
