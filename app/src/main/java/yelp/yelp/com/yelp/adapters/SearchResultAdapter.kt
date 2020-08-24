package yelp.yelp.com.yelp.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yelp.yelp.com.yelp.R
import yelp.yelp.com.yelp.models.Business


class SearchResultAdapter(val context: Context, val businesses: ArrayList<Business>): RecyclerView.Adapter<SearchResultAdapter.BusinessHolder>() {

    var mOnReviewClickListener: OnReviewClickListener? = null
    var mOnStatusInitialize: OnStatusInitialize? = null
    var mOnClickContactListener: OnClickContactListener? = null
    var mOnClickItemListener: OnClickItemListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessHolder {

        return BusinessHolder(LayoutInflater.from(context).inflate(R.layout.item_business, parent, false))
    }

    override fun getItemCount() = businesses.size

    override fun onBindViewHolder(holder: BusinessHolder, position: Int) {

        val business = businesses.get(position)

        Glide.with(context).load(business.image_url).into(holder.image1)

        holder.contact.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
        holder.reviewsLabel.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
        holder.reviews.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)

        holder.reviews.text = business.review_count.toString()
        holder.price.text = business.price
        holder.businessName.text = business.name
        holder.businessName.text = business.name
        holder.rating.rating = business.rating
        holder.contact.text = business.display_phone
        holder.address.text = business.location.display_address.joinToString(separator = " ")
        holder.category.text = business.categories.joinToString(separator= ", ") {
            it.title
        }
        holder.expandedView.visibility = if ( business.expanded ) View.VISIBLE else View.GONE

        mOnStatusInitialize?.onInitialized(business, holder.status)


        holder.contact.setOnClickListener{

            mOnClickContactListener?.onClicked(business, position)

        }

        holder.root.setOnClickListener{
            mOnClickItemListener?.onClicked(business, position)
        }


    }


    interface OnReviewClickListener {
        fun onClicked(item: Business, i: Int)
    }
    // for status
    interface OnStatusInitialize {
        fun onInitialized(item: Business, v: View)
    }

    interface OnClickContactListener {
        fun onClicked(itme: Business, i: Int)
    }


    interface OnClickItemListener {
        fun onClicked(item: Business, i: Int)
    }

    class BusinessHolder(v: View) : RecyclerView.ViewHolder(v) {

        var image1: ImageView
        var image2: ImageView
        var image3: ImageView
        var rating: RatingBar
        var businessName: TextView
        var price: TextView
        var contact: TextView
        var category: TextView
        var reviews: TextView
        var status: TextView
        var address: TextView
        var reviewsLabel: TextView
        var root: CardView
        var expandedView: LinearLayout
        init {
            image1 = v.findViewById(R.id.image1)
            image2 = v.findViewById(R.id.image2)
            image3 = v.findViewById(R.id.image3)
            businessName = v.findViewById(R.id.businessName)
            price = v.findViewById(R.id.price)
            rating = v.findViewById(R.id.rating)
            contact = v.findViewById(R.id.contact)
            category = v.findViewById(R.id.category)
            reviews = v.findViewById(R.id.reviews)
            address = v.findViewById(R.id.address)
            status = v.findViewById(R.id.status)
            root = v.findViewById(R.id.root)
            reviewsLabel = v.findViewById(R.id.reviewsLabel)
            expandedView = v.findViewById(R.id.expandedView)
        }
    }
}