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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yelp.yelp.com.yelp.R
import yelp.yelp.com.yelp.models.Review

import yelp.yelp.com.yelp.utils.DateUtils

class ReviewsAdapter(val context: Context, val businesses: ArrayList<Review>): RecyclerView.Adapter<ReviewsAdapter.BusinessHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessHolder {

        return BusinessHolder(LayoutInflater.from(context).inflate(R.layout.item_review, parent, false))
    }

    override fun getItemCount() = businesses.size

    override fun onBindViewHolder(holder: BusinessHolder, position: Int) {



        val item = businesses[position]


        Glide.with(context).load(item.user.image_url).into(holder.profile)

        holder.name.text = item.user.name
        holder.review.text = item.text
        holder.rating.rating = item.rating
        holder.date.text = DateUtils.getStandardDateFormat(item.time_created)

    }



    class BusinessHolder(v: View) : RecyclerView.ViewHolder(v) {

        var profile: ImageView
        var name: TextView
        var rating: RatingBar
        var review: TextView
        var date: TextView
        init {
            profile = v.findViewById(R.id.profile)
            name = v.findViewById(R.id.name)
            rating = v.findViewById(R.id.rating)
            review = v.findViewById(R.id.review)
            date = v.findViewById(R.id.date)
        }
    }
}