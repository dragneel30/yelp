package yelp.yelp.com.yelp.models

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.utils.Deserializer

data class SearchResult (
    var businesses: ArrayList<Business>,
    val total: Int,

//    @SerializedName("center")
//    @JsonAdapter(Deserializer.RegionDeserializer::class)
//    val center: LatLng

    val region: Region
)