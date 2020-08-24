package yelp.yelp.com.yelp.adapters

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import yelp.yelp.com.yelp.R
import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.models.BusinessClusterItem

class BusinessInfoWindowAdapter(val context: AppCompatActivity): GoogleMap.InfoWindowAdapter {

    var mOnStatusInitialize: OnStatusInitialize? = null
    var mOnDetaislShowListener: OnDetaislShowListener? = null

    override fun getInfoWindow(p0: Marker): View {
        return getInflatedLayout(p0)
    }


    override fun getInfoContents(p0: Marker): View? {

        return null
    }

    fun getInflatedLayout(marker: Marker): View {


        val clusterItem = (marker.tag as BusinessClusterItem)
        val business = clusterItem.business

        mOnDetaislShowListener?.onDetailShow(clusterItem)

        val v = context.layoutInflater.inflate(R.layout.info_window, null)

        val image1 = v.findViewById<ImageView>(R.id.image1)

        val rating = v.findViewById<RatingBar>(R.id.rating)
        val contact = v.findViewById<TextView>(R.id.contact)
        val category = v.findViewById<TextView>(R.id.category)
        val name = v.findViewById<TextView>(R.id.name)
        val reviews = v.findViewById<TextView>(R.id.reviews)
        val status = v.findViewById<TextView>(R.id.status)


        contact.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)

        Glide.with(context).asBitmap().load(business.image_url).into(image1)


        contact.text = business.display_phone
        rating.rating = business.rating
        name.text = business.name
        contact.text = business.display_phone
        reviews.text = business.review_count.toString()
        category.text = business.categories.joinToString(separator= ", ") { it.title }

        mOnStatusInitialize?.onInitialized(business, status)



        return v

    }


    interface OnStatusInitialize {
        fun onInitialized(item: Business, v: View)
    }






    interface OnDetaislShowListener {

        fun onDetailShow(item: BusinessClusterItem)
    }

}