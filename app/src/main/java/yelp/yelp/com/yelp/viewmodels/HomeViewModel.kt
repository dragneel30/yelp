package yelp.yelp.com.yelp.viewmodels

import android.app.Application
import android.content.DialogInterface
import androidx.lifecycle.*
import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.models.SearchResult

import yelp.yelp.com.yelp.repositories.SearchResultRepository
import yelp.yelp.com.yelp.utils.DataUtils

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    enum class SORT(val code :String){

        BY_DISTANCE("Sort By Distance"),
        BY_RATING("Sort By Ratings")

    }

    private val mSearchResult = MutableLiveData<SearchResult>()
    private val mIsLoading = MutableLiveData<Boolean>()

    fun getIsLoading(): LiveData<Boolean> {
        return mIsLoading
    }

    fun getSearchResult(): LiveData<SearchResult>{
        return mSearchResult
    }


    fun fetchBusiness(auth: String, latitude: Double, longitude: Double, term: String) {

        mIsLoading.postValue(true)
        SearchResultRepository.getSearchResult(auth, latitude, longitude, term, object: SearchResultRepository.SearchResultCallback {

            override fun onFailure(t: Throwable) {
                t.printStackTrace()
                mSearchResult.postValue(null)
                mIsLoading.postValue(false)
            }

            override fun onResponseSuccess(t: SearchResult?) {

                mSearchResult.postValue(t)
                mIsLoading.postValue(false)
            }

            override fun onResponseFailure(error: String) {
                mSearchResult.postValue(null)
                mIsLoading.postValue(false)
            }
        })

    }

    fun sort(sortType: SORT) {

        if ( sortType == SORT.BY_DISTANCE ) {

            mSearchResult.value!!.businesses = DataUtils.sortArray(mSearchResult.value!!.businesses, { a: Business, b: Business ->  if (a.distance >= b.distance) 1 else -1  })


        } else if ( sortType == SORT.BY_RATING ) {

            mSearchResult.value!!.businesses = DataUtils.sortArray(mSearchResult.value!!.businesses, { a: Business, b: Business ->  if (a.rating >= b.rating) 1 else -1  })

        }


        mSearchResult.postValue(mSearchResult.value)
    }


    val onSort = object: DialogInterface.OnClickListener {

        override fun onClick(d: DialogInterface?, which: Int) {

            if ( which == 0 ) {

                sort(SORT.BY_DISTANCE)
            }
            else if ( which == 1 ) {


                sort(SORT.BY_RATING)
            }

        }



    }


//    fun onPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//
//        if ( grantResults.size > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == PermissionCode.GPS.code ) {
//
//
//
//        }
//
//    }


}