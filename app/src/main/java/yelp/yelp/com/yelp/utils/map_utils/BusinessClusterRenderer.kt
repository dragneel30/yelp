package yelp.yelp.com.yelp.utils.map_utils

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import yelp.yelp.com.yelp.models.BusinessClusterItem

class BusinessClusterRenderer(val context: Context, val map: GoogleMap, clusterManager: ClusterManager <BusinessClusterItem>) : DefaultClusterRenderer<BusinessClusterItem>(context, map, clusterManager ) {

    override fun onClusterItemRendered(clusterItem: BusinessClusterItem?, marker: Marker?) {
        super.onClusterItemRendered(clusterItem, marker)


        marker?.tag = clusterItem
    }





}