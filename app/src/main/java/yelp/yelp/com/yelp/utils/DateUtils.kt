package yelp.yelp.com.yelp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val mDays = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    private val mMobileDateFormat = SimpleDateFormat("MM-dd-yyyy")
    private val mWebFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun getCurrentDayIndex(): Int {

        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    }

    fun intToDayOfWeek(day: Int): String {
        return mDays[day]
    }
    fun getStandardDateFormat(date: String): String {

        val date = mWebFormat.parse(date)

        return mMobileDateFormat.format(date)

    }

    fun militaryTo12(milTime: String): String{

        val date = SimpleDateFormat("hhmm").parse(milTime)

        val sdf = SimpleDateFormat("hh:mm a")

        return sdf.format(date)
    }


}