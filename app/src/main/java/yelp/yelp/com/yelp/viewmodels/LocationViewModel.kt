package yelp.yelp.com.yelp.viewmodels


import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import yelp.yelp.com.yelp.utils.PermissionCode

class LocationViewModel(application: Application): AndroidViewModel(application) {

    private var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val mLocation = MutableLiveData<Location>()

    fun getLocation() : LiveData<Location> {

        return mLocation
    }


    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    private fun setLocationData(location: Location) {
        this.mLocation.postValue(location)
    }

    override fun onCleared() {
        super.onCleared()

        cleanUp()
    }


    fun cleanUp() {

        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun onPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if ( grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == PermissionCode.GPS.code ) {

            startLocationUpdates()

        }

    }

}
