package yelp.yelp.com.yelp.utils

import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonDeserializer
import java.lang.reflect.Type


object Deserializer {
    object RegionDeserializer : JsonDeserializer<LatLng> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LatLng {
            try {
                val center = json.asJsonObject.get("region").asJsonObject.get("center").asJsonObject

                return LatLng(
                    center.get("latitude").asDouble,
                    center.get("longitude").asDouble
                )
            } catch (e: Exception) {
                return LatLng(0.0, 0.0)

            }
        }
    }
}