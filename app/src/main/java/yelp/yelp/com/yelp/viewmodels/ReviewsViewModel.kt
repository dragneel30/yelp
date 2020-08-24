package yelp.yelp.com.yelp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import yelp.yelp.com.yelp.models.*
import yelp.yelp.com.yelp.repositories.ReviewsRepository

class ReviewsViewModel(application: Application) : AndroidViewModel(application) {

    private val mReviews = MutableLiveData<ReviewResult>()

    fun getReviews(): LiveData<ReviewResult> {
        return mReviews
    }

    fun fetchReviews(auth: String, id: String) {

        ReviewsRepository.getReviews(auth, id, object: ReviewsRepository.ReviewsCallback{
            override fun onResponseSuccess(result: ReviewResult?) {
                mReviews.postValue(result)
            }


            override fun onFailure(t: Throwable) {
                mReviews.postValue(null)
                t.printStackTrace()

            }

            override fun onResponseFailure(error: String) {
                println(error)

                mReviews.postValue(null)
            }
        })

    }
}