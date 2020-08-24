package yelp.yelp.com.yelp.repositories

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelp.yelp.com.yelp.interfaces.YelpBusinessAPIService
import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.models.SearchResult

object BusinessRepository {
    interface BusinessCallback {

        fun onResponseSuccess(searchResult: Business?)
        fun onResponseFailure(error: String)
        fun onFailure(t: Throwable)

    }

    fun getBusiness(auth: String, id: String, callback: BusinessCallback): MutableLiveData<Business> {

        var searchResult: MutableLiveData<Business> = MutableLiveData()

        val request = ServiceBuilder.buildService(YelpBusinessAPIService::class.java)

        val call = request.getBusiness(auth, id)

        call.enqueue(object : Callback<Business> {
            override fun onResponse(call: Call<Business>, response: Response<Business>) {

                println("done done done")
                if ( response.isSuccessful ) {

                    callback.onResponseSuccess(response.body())
                } else {

                    callback.onResponseFailure(response.message())
                }


            }
            override fun onFailure(call: Call<Business>, t: Throwable) {

                callback.onFailure(t)

            }

        })

        return searchResult;

    }

}
