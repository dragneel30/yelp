package yelp.yelp.com.yelp.models

data class Day  (

    val is_overnight: Boolean,
    val start: String,
    val end: String,
    val day: Int
)