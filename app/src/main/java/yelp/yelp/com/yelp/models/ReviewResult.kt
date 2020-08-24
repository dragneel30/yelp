package yelp.yelp.com.yelp.models

data class ReviewResult (
    val reviews: ArrayList<Review>,
    val total: Int
)