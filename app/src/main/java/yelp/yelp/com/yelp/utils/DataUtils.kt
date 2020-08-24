package yelp.yelp.com.yelp.utils

object DataUtils {

    fun <T> sortArray (array: ArrayList<T>, pred: (T, T) -> Int ): ArrayList<T> {

        val comparator = Comparator { a: T, b: T->
            return@Comparator pred(a,b)
        }
        val copy = arrayListOf<T>().apply { addAll(array) }
        copy.sortWith(comparator)
        return copy
    }
}