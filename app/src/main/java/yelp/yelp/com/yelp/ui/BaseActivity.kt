package yelp.yelp.com.yelp.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

open abstract class BaseActivity : AppCompatActivity() {


    abstract fun initViews()
    abstract fun setLoading(loading: Boolean)
    abstract fun initObservers()

}