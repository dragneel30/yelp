package yelp.yelp.com.yelp.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_details.*
import yelp.yelp.com.yelp.R
import yelp.yelp.com.yelp.adapters.ReviewsAdapter
import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.models.Review
import yelp.yelp.com.yelp.models.ReviewResult
import yelp.yelp.com.yelp.utils.Constants.KEY_ID
import yelp.yelp.com.yelp.utils.DateUtils
import yelp.yelp.com.yelp.utils.PermissionCode
import yelp.yelp.com.yelp.viewmodels.BusinessViewModel
import yelp.yelp.com.yelp.viewmodels.LocationViewModel
import yelp.yelp.com.yelp.viewmodels.ReviewsViewModel

class DetailsActivity : BaseActivity(), View.OnClickListener {

    lateinit var mId: String
    lateinit var mBusiness: Business
    lateinit var mBusinessName: TextView
    lateinit var mOpeningHoursLabel: TextView
    lateinit var mWazeNav: TextView
    lateinit var mReviewLabel: TextView
    lateinit var mReviewList: RecyclerView
    lateinit var mBusinessPhoto: ImageView
    lateinit var mViewParent: LinearLayout
    lateinit var mOpeningHoursRoot: CardView
    lateinit var mReviewsAdapter: ReviewsAdapter
    lateinit var mStatus: TextView
    lateinit var mCategories: TextView
    lateinit var mContact: TextView
    lateinit var mDetailsViewModel: BusinessViewModel
    lateinit var mReviewsViewModel: ReviewsViewModel
    var mReviews = ArrayList<Review>()
    var mOpeningHoursViews = ArrayList<TextView>()
    var mPhotos = ArrayList<ImageView>()

    private lateinit var mLoading: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        mId = intent.getStringExtra(KEY_ID)

        initViews()
        initObservers()
        initReviewList()

        mDetailsViewModel.fetchBusiness(getString(R.string.yelp_token), mId)
        mReviewsViewModel.fetchReviews(getString(R.string.yelp_token), mId)
    }

    override fun setLoading(loading: Boolean) {
        if (loading) {
            mViewParent.visibility = View.GONE
            mLoading.visibility = View.VISIBLE
        } else {

            mViewParent.visibility = View.VISIBLE
            mLoading.visibility = View.GONE
        }
    }

    fun initReviewList() {

        mReviewsAdapter = ReviewsAdapter(this, mReviews)

        (mReviewList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        mReviewList.layoutManager = LinearLayoutManager(this)

        mReviewList.adapter = mReviewsAdapter

        mReviewList.setHasFixedSize(true)
    }


    override fun initObservers() {

        mDetailsViewModel.getIsLoading().observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean) {

                setLoading(t)
            }
        })

        mDetailsViewModel.getBusiness().observe(this, object : Observer<Business> {
            override fun onChanged(business: Business) {

                mBusiness = business

                if ( business != null ) {
                    for ((index, photo_url) in business.photos.withIndex()) {

                        Glide.with(this@DetailsActivity).load(photo_url).into(mPhotos[index])
                    }
                    Glide.with(this@DetailsActivity).load(business.image_url).into(mBusinessPhoto)

                    if (business.is_closed) {

                        mStatus.text = resources.getString(R.string.closed)
                        mStatus.setTextColor(ContextCompat.getColor(this@DetailsActivity, R.color.status_closed))

                    } else {

                        mStatus.text = resources.getString(R.string.open_now)
                        mStatus.setTextColor(ContextCompat.getColor(this@DetailsActivity, R.color.status_open))
                    }


                    mBusinessName.text = business.name
                    mContact.text = business.display_phone
                    mCategories.text = business.categories.joinToString(separator = ", ") { it.title }

                    initHours()
                } else {
                    Toast.makeText(this@DetailsActivity, getString(R.string.api_err), Toast.LENGTH_LONG).show()
                }

            }
        })

        mReviewsViewModel.getReviews().observe(this, object : Observer<ReviewResult> {
            override fun onChanged(t: ReviewResult?) {

                if ( t!= null ) {

                    mReviews.clear()
                    mReviews.addAll(t!!.reviews)
                    mReviewsAdapter.notifyDataSetChanged()
                    mReviewLabel.visibility = View.VISIBLE

                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.contact -> {
                initiatePhoneCall()
            }
            R.id.wazeNav -> {
                navToWaze()
            }
        }
    }

    override fun initViews() {

        mOpeningHoursViews.add(findViewById(R.id.mon))
        mOpeningHoursViews.add(findViewById(R.id.tue))
        mOpeningHoursViews.add(findViewById(R.id.wed))
        mOpeningHoursViews.add(findViewById(R.id.thurs))
        mOpeningHoursViews.add(findViewById(R.id.fri))
        mOpeningHoursViews.add(findViewById(R.id.sat))
        mOpeningHoursViews.add(findViewById(R.id.sun))
        mStatus = findViewById(R.id.status)
        mBusinessName = findViewById(R.id.businessName)
        mReviewLabel = findViewById(R.id.reviewLabel)
        mLoading = findViewById(R.id.loading)
        mPhotos.add(findViewById(R.id.image1))
        mPhotos.add(findViewById(R.id.image2))
        mPhotos.add(findViewById(R.id.image3))
        mOpeningHoursLabel = findViewById(R.id.openingHoursLabel)
        mWazeNav = findViewById(R.id.wazeNav)
        mCategories = findViewById(R.id.categories)
        mViewParent = findViewById(R.id.viewParent)
        mContact = findViewById(R.id.contact)
        mBusinessPhoto = findViewById(R.id.businessPhoto)
        mOpeningHoursRoot = findViewById(R.id.openingHoursRoot)
        mReviewList = findViewById(R.id.reviewList)
        mDetailsViewModel = ViewModelProviders.of(this).get(BusinessViewModel::class.java)
        mReviewsViewModel = ViewModelProviders.of(this).get(ReviewsViewModel::class.java)
        mContact.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
        mWazeNav.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
        mContact.setOnClickListener(this)
        mWazeNav.setOnClickListener(this)
        mLoading.indeterminateDrawable = DoubleBounce()
    }

    @SuppressLint("MissingPermission")
    fun initiatePhoneCall() {

        try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mBusiness.display_phone))
            startActivity(intent)

        } catch (e: Exception) {
            ActivityCompat.requestPermissions(
                this@DetailsActivity, arrayOf(android.Manifest.permission.CALL_PHONE),
                PermissionCode.CALL.code
            )

        }
    }

    fun initHours() {

        if (mBusiness.hours != null && mBusiness.hours.size > 0) {
            val hours = mBusiness.hours[0]

            for (x in 0 until 7) {
                try {
                    mOpeningHoursViews[x].text =
                        DateUtils.intToDayOfWeek(x) + ": " + DateUtils.militaryTo12(hours.open[x].start) + " - " + DateUtils.militaryTo12(
                            hours.open[x].end
                        )
                } catch (e: Exception) {
                    mOpeningHoursViews[x].text = DateUtils.intToDayOfWeek(x) + ": Closed"
                }
            }
        } else {

            mOpeningHoursLabel.text = getString(R.string.opening_hours_err)
            mOpeningHoursLabel.setTextColor(resources.getColor(R.color.status_closed))
            mOpeningHoursRoot.visibility = View.GONE
        }

    }

    fun navToWaze() {
        try {

            val uri = "waze://?ll=${mBusiness.coordinates.latitude}, ${mBusiness.coordinates.longitude}&navigate=yes"
            var intent =  Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        } catch (ex: ActivityNotFoundException) {
            // If Waze is not installed, open it in Google Play:
            var intent  =  Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
            startActivity(intent);
        }
    }

}
