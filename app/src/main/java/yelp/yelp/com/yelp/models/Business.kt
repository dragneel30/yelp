package yelp.yelp.com.yelp.models

import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class Business (
    val id: String,
    val name: String,
    val is_closed: Boolean,
    val coordinates: LatLng,
    val location: Location,
    val price: String,
    val rating: Float,
    val categories: Array<Category>,
    val review_count: Int,
    val distance: Double,
    val image_url: String,
    val display_phone: String,
    var expanded: Boolean = false,
    val hours: ArrayList<OpeningHour>,
    val photos: ArrayList<String>
) : Serializable

