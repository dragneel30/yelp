package yelp.yelp.com.yelp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import yelp.yelp.com.yelp.R
import yelp.yelp.com.yelp.models.SearchResult
import yelp.yelp.com.yelp.adapters.SearchResultAdapter
import yelp.yelp.com.yelp.models.Business
import yelp.yelp.com.yelp.viewmodels.HomeViewModel
import com.google.maps.android.clustering.ClusterManager
import yelp.yelp.com.yelp.utils.map_utils.BusinessClusterRenderer
import yelp.yelp.com.yelp.adapters.BusinessInfoWindowAdapter
import yelp.yelp.com.yelp.models.BusinessClusterItem
import yelp.yelp.com.yelp.utils.Constants.KEY_ID
import yelp.yelp.com.yelp.utils.PermissionCode
import yelp.yelp.com.yelp.viewmodels.LocationViewModel


class MainActivity : BaseActivity(), OnMapReadyCallback, View.OnClickListener {


    private lateinit var mMap: GoogleMap
    private lateinit var mSearchResultRecyclerView: RecyclerView
    private lateinit var mListViewRoot: LinearLayout
    private lateinit var mSearchResultAdapter: SearchResultAdapter
    private lateinit var mBusinessInfoWindowAdapter: BusinessInfoWindowAdapter
    private lateinit var mHomeViewModel: HomeViewModel
    private lateinit var mLocationViewModel: LocationViewModel
    private lateinit var mLoading: ProgressBar
    private lateinit var mViewToggleImageView: ImageView
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var mClusterManager: ClusterManager<BusinessClusterItem>
    private lateinit var mClusterRenderer: BusinessClusterRenderer
    private lateinit var mCurrentLocation: Location
    private lateinit var mSearchBar: EditText
    private lateinit var mSortButton: TextView
    private lateinit var mLocationLiveData: LiveData<Location>

    private var clickedMarker: BusinessClusterItem? = null
    private var mSearchResult = ArrayList<Business>()
    private var mIsMapView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initResultList()
        initObservers()

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),PermissionCode.GPS.code)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE),PermissionCode.CALL.code)

        Toast.makeText(this, getString(R.string.locating_message), Toast.LENGTH_LONG).show()
    }

    override fun initObservers() {

        mHomeViewModel.getSearchResult().observe(this, object: Observer<SearchResult> {
            override fun onChanged(t: SearchResult?) {

                if ( t != null ) {

                    mSearchResult.clear()
                    mSearchResult.addAll(t!!.businesses)

                    mSearchResultAdapter.notifyDataSetChanged()

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(t.region.center))
                    mMap.clear()

                    for (business in t.businesses) {
                        mClusterManager.addItem(BusinessClusterItem(business))
                    }

                    mClusterManager.cluster()


                } else {
                    Toast.makeText(this@MainActivity, getString(R.string.api_err), Toast.LENGTH_LONG).show()
                }
            }
        })

        mHomeViewModel.getIsLoading().observe(this, object: Observer<Boolean> {
            override fun onChanged(t: Boolean) {

                setLoading(t)
            }
        })
        mLocationLiveData = mLocationViewModel.getLocation()

        mLocationLiveData.observe(this, object: Observer<Location> {

            override fun onChanged(t: Location) {

                if ( t != null ) {
                    mCurrentLocation = t
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(t.latitude, t.longitude), 10.0f))
                    mLocationViewModel.cleanUp()
                    mLocationLiveData.removeObserver(this)
                    setLoading(false)
                    Toast.makeText(this@MainActivity, getString(R.string.locate_suc_message), Toast.LENGTH_LONG).show()
                }
            }

        })

    }
    override fun setLoading(isLoading: Boolean) {

        mLoading.visibility = if ( isLoading ) View.VISIBLE else View.GONE
    }

    fun initResultList() {

        mSearchResultAdapter = SearchResultAdapter(this, mSearchResult)

        mSearchResultAdapter.mOnReviewClickListener = object: SearchResultAdapter.OnReviewClickListener {

            override fun onClicked(item: Business, i: Int) {

                item.expanded = !item.expanded

                mSearchResultAdapter.notifyItemChanged(i)

            }
        }

        mSearchResultAdapter.mOnStatusInitialize = object: SearchResultAdapter.OnStatusInitialize {
            override fun onInitialized(item: Business, v: View) {


                val textview = v as TextView

                if ( item.is_closed ) {

                    textview.text = resources.getString(R.string.closed)
                    textview.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.status_closed))

                } else {

                    textview.text = resources.getString(R.string.open_now)
                    textview.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.status_open))
                }
            }
        }

        mSearchResultAdapter.mOnClickContactListener = object: SearchResultAdapter.OnClickContactListener {
            @SuppressLint("MissingPermission")
            override fun onClicked(item: Business, i: Int) {
                initiatePhoneCall(item.display_phone)
            }
        }


        mSearchResultAdapter.mOnClickItemListener = object: SearchResultAdapter.OnClickItemListener {

            override fun onClicked(item: Business, i: Int) {

                val intent = Intent(this@MainActivity, DetailsActivity::class.java)

                intent.putExtra(KEY_ID, item.id)

                startActivity(intent)

            }

        }

        (mSearchResultRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        mSearchResultRecyclerView.layoutManager = LinearLayoutManager(this)

        mSearchResultRecyclerView.adapter = mSearchResultAdapter

        mSearchResultRecyclerView.setHasFixedSize(true)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        mLocationViewModel.onPermissionResult(requestCode, permissions, grantResults)

    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mClusterManager = ClusterManager(this, mMap)
        mBusinessInfoWindowAdapter = BusinessInfoWindowAdapter(this)
        mClusterRenderer = BusinessClusterRenderer(this, mMap, mClusterManager)
        mClusterManager.setRenderer(mClusterRenderer)
        mMap.setOnMarkerClickListener(mClusterManager)
        mMap.setOnCameraChangeListener(mClusterManager)

        mClusterManager.setOnClusterClickListener {


            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                it.position,
                Math.floor(mMap.getCameraPosition().zoom + 2.0).toString().toFloat())
            )

            return@setOnClusterClickListener true
        }

        mBusinessInfoWindowAdapter.mOnDetaislShowListener = object: BusinessInfoWindowAdapter.OnDetaislShowListener {


            override fun onDetailShow(item: BusinessClusterItem) {

                clickedMarker = item
            }

        }

        mMap.setInfoWindowAdapter(mBusinessInfoWindowAdapter)

    }


    override fun onClick(v: View?) {

        when(v!!.id) {
            R.id.viewToggleImageView -> {
                toggleView()
            }
            R.id.sortButton -> {
                openSortDialog()
            }
        }
    }

    // open sort option dialog
    fun openSortDialog() {

        val sortOption = resources.getStringArray(R.array.sort_options)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.sort_option_title))
            .setItems(sortOption, mHomeViewModel.onSort)
            .show()

    }

    // toggle map and list view
    fun toggleView() {

        mIsMapView = !mIsMapView

        if ( mIsMapView ) {
            mViewToggleImageView.setImageDrawable(getDrawable(R.drawable.listview))
            mMapFragment.view!!.visibility = View.VISIBLE
            mListViewRoot.visibility = View.GONE
        } else {
            mViewToggleImageView.setImageDrawable(getDrawable(R.drawable.mapview))
            mMapFragment.view!!.visibility = View.GONE
            mListViewRoot.visibility = View.VISIBLE
        }
    }


    // initiate search
    private fun doSearch() {

        if ( mCurrentLocation != null ) {

            mHomeViewModel.fetchBusiness(
                getString(R.string.yelp_token),
                mCurrentLocation.latitude,
                mCurrentLocation.longitude,
                mSearchBar.text.toString()
            )

        } else {

            Toast.makeText(this, getString(R.string.cant_locate_err), Toast.LENGTH_LONG).show()

        }
    }

    // check if user presses enter
    val onSearchBarEdited = object: View.OnKeyListener {
        override fun onKey(v: View, keyCode: Int, p2: KeyEvent): Boolean {
            if ((p2.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                doSearch()

                return true
            }
            return false
        }
    }

    @SuppressLint("MissingPermission")
    fun initiatePhoneCall(num: String) {

        try {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num))
            startActivity(intent)

        } catch(e: Exception) {

            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.CALL_PHONE),PermissionCode.CALL.code)

        }
    }
    override fun initViews() {

        mMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mMapFragment.getMapAsync(this)
        mSearchResultRecyclerView = findViewById(R.id.searchResultRecyclerView)
        mViewToggleImageView = findViewById(R.id.viewToggleImageView)
        mSearchBar = findViewById(R.id.searchBar)
        mLoading = findViewById(R.id.loading)
        mListViewRoot = findViewById(R.id.listViewRoot)
        mSortButton = findViewById(R.id.sortButton)
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mLocationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
        mViewToggleImageView.setOnClickListener(this)
        mSortButton.setOnClickListener(this)
        mSearchBar.setOnKeyListener(onSearchBarEdited)
        mLoading.indeterminateDrawable = DoubleBounce()
    }
}
