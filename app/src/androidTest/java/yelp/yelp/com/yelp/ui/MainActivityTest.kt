package yelp.yelp.com.yelp.ui

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before

import android.R.attr.action
import android.R.attr.visibility
//import androidx.test
import android.view.KeyEvent
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Rule
import org.junit.Test
import yelp.yelp.com.yelp.R

class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    var mActivity = mActivityTestRule?.activity

    @Before
    fun setUp() {

        mActivity = mActivityTestRule.activity

    }
    @Test
    fun testToggleImage() {

        // click toggle image
        Espresso.onView(withId(R.id.viewToggleImageView)).perform(click())
        // check if the view is toggled to the list view
        Espresso.onView(withId(R.id.listViewRoot)).check(matches(isDisplayed()))
        // click again to check
        Espresso.onView(withId(R.id.viewToggleImageView)).perform(click())
        // check if the view goes back to map view
        Espresso.onView(withId(R.id.map)).check(matches(isDisplayed()))


        Espresso.onView(withId(R.id.listViewRoot)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.listViewRoot)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.map)).check(matches(isDisplayed()))
    }
    @After
    fun tearDown() {
        mActivity = null
    }
}