package yelp.yelp.com.yelp.repositories

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelp.yelp.com.yelp.interfaces.YelpBusinessAPIService
import yelp.yelp.com.yelp.interfaces.YelpReviewsAPIService
import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.models.Review
import yelp.yelp.com.yelp.models.ReviewResult
import yelp.yelp.com.yelp.models.SearchResult

object ReviewsRepository {

    interface ReviewsCallback {

        fun onResponseSuccess(searchResult: ReviewResult?)
        fun onResponseFailure(error: String)
        fun onFailure(t: Throwable)

    }

    fun getReviews(auth: String, id: String, callback: ReviewsCallback): MutableLiveData<ReviewResult> {

        var searchResult: MutableLiveData<ReviewResult> = MutableLiveData()

        val request = ServiceBuilder.buildService(YelpReviewsAPIService::class.java)

        val call = request.getReviews(auth, id)

        call.enqueue(object : Callback<ReviewResult> {
            override fun onResponse(call: Call<ReviewResult>, response: Response<ReviewResult>) {

                if ( response.isSuccessful ) {

                    callback.onResponseSuccess(response.body())
                } else {

                    callback.onResponseFailure(response.message())
                }


            }

            override fun onFailure(call: Call<ReviewResult>, t: Throwable) {
                t.printStackTrace()
                callback.onFailure(t)
            }


        })

        return searchResult

    }

}
