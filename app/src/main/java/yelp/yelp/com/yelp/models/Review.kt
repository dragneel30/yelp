package yelp.yelp.com.yelp.models

data class Review (
    val id: String,
    val text: String,
    val rating: Float,
    val user: User,
    val time_created: String
)