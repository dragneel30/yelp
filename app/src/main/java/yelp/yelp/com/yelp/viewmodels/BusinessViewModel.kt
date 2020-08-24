package yelp.yelp.com.yelp.viewmodels

import android.app.Application

import androidx.lifecycle.*

import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.repositories.BusinessRepository

class BusinessViewModel(application: Application) : AndroidViewModel(application) {


    private val mIsLoading = MutableLiveData<Boolean>()
    private val mBusiness = MutableLiveData<Business>()

    fun getIsLoading(): LiveData<Boolean> {
        return mIsLoading
    }

    fun getBusiness(): LiveData<Business> {
        return mBusiness
    }

    fun fetchBusiness(auth: String, id: String) {

        mIsLoading.postValue(true)
        BusinessRepository.getBusiness(auth, id, object: BusinessRepository.BusinessCallback {
            override fun onResponseSuccess(result: Business?) {
                mBusiness.postValue(result)
                mIsLoading.postValue(false)
            }


            override fun onFailure(t: Throwable) {

                mBusiness.postValue(null)
                mIsLoading.postValue(false)
                t.printStackTrace()
            }

            override fun onResponseFailure(error: String) {

                mBusiness.postValue(null)
                mIsLoading.postValue(false)
            }
        })

    }
}