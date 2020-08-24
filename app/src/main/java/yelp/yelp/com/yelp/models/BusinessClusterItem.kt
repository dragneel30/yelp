package yelp.yelp.com.yelp.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class BusinessClusterItem (val business: Business) : ClusterItem {


    override fun getPosition(): LatLng {
        return business.coordinates

    }




}

