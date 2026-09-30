package com.dapl.halaladvisor.Activity

import android.Manifest
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.dapl.halaladvisor.Adapter.NavigationListAdapter
import com.dapl.halaladvisor.Adapter.NewNearByRestaurantAdapter
import com.dapl.halaladvisor.JSONParser
import com.dapl.halaladvisor.MainActivity
import com.dapl.halaladvisor.Model.AddressObject
import com.dapl.halaladvisor.Model.LastSearchLocationObject
import com.dapl.halaladvisor.Model.NearbyRestaurantResponse
import com.dapl.halaladvisor.Model.Result
import com.dapl.halaladvisor.Model.addreviewres.AddReviewRes
import com.dapl.halaladvisor.Model.featuredimages.FeaturedImagesRes
import com.dapl.halaladvisor.R
import com.dapl.halaladvisor.Utility.LogUtils
import com.dapl.halaladvisor.Utility.PrefUtils
import com.dapl.halaladvisor.Utility.Utils
import com.dapl.halaladvisor.application_data.AppData
import com.dapl.halaladvisor.extra.*
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.plus.People.LoadPeopleResult
import com.google.android.gms.plus.Plus
import com.google.android.material.navigation.NavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class SwipeHomeActivity : AppCompatActivity(), LocationListener, DrawerListener,
    View.OnClickListener, AdapterView.OnItemClickListener, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, GestureDetector.OnGestureListener {
    private val PERMISSION_REQUEST_CODE: Int=11
    lateinit var nav_nameId_logout: Array<String>
    lateinit var nav_nameId_login: Array<String>
    var context: Context? = null
   var TAG_REQUEST_RESTAURANT_SEARCH = "request_restaurant_searchh";
    var navUserImage: ImageView? = null
    var newNearByRestaurantAdapter: NewNearByRestaurantAdapter? = null
    var tv_navUsername: TextView? = null
    var nearbyRest: ArrayList<Result?> = ArrayList<Result?>()
    var edSearch: LinearLayout? = null
    var user_name: TextView? = null
    var no_record_found: TextView? = null
    var rv_nearby: RecyclerView? = null
    var linear_nearBy: LinearLayout? = null
    var rell: RelativeLayout? = null
    var drawerLayout: DrawerLayout? = null
    var navigationView: NavigationView? = null
    var header: ConstraintLayout? = null
    var quick_view: TextView? = null
    var imageView7: ImageView? = null
    private var foldState = 0 // 0: Full View (Expanded), 1: Half View (Collapsed + Bottom Layout)

    // View navHeader;
    var AUTOCOMPLETE_REQUEST_CODE = 1256
    var isListVisible = false
    var location: Location? = null
    var latitude = 0.0
    var longitude = 0.0

    // How many Geocoder should return our GPSTracker
    var geocoderMaxResults = 1

    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private var provider_info: String? = null

    // flag for GPS Status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS Tracking is enabled
    var isGPSTrackingEnabled = false
    var nav_listView: ListView? = null
    var mProgressDialog: ProgressDialog? = null
    var DRAWER_ACTION = -1
    var DRAWER_ACTION_HOME = 0
    var DRAWER_ACTION_SCAN_GROCERIES = 1
    var DRAWER_ACTION_MY_FAVOURITES = 2
    var DRAWER_ACTION_PROFILE = 3
    var DRAWER_ACTION_OFFER = 4
    var DRAWER_ACTION_SEND_FEEDBACK = 5
    var DRAWER_ACTION_RATE_US = 6
    var DRAWER_ACTION_ADVERTISE_ON_HALAL_APP = 7
    var DRAWER_ACTION_ABOUT_HALAL_APP = 8
    var DRAWER_ACTION_LOG_OUT = 9

    //int DRAWER_ACTION_PICK_UP = 1;
    // int DRAWER_ACTION_DELIVERY = 2;
    //  int DRAWER_ACTION_SHARE = 4;
    var apiService: ApiService? = null
    var navIcon: ImageView? = null
    var selectedCateroryType = "2"
    var gestureScanner: GestureDetector? = null
    var swipeUpIcon: ImageView? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mFirebaseAnalytics: FirebaseAnalytics? = null
    var progressBar: ProgressBar? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
          //  sendNotification(this)
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_home)
        context = this
        progressBar = findViewById(R.id.progressBar)
        hideProgressBar()
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API)
            .build()
        try {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // initUi();
        //getLocation();
        PrefUtils.saveActivityOpenFromNotification(context, false)
        //        setView(HomeFragment.newInstance(true, selectedCateroryType), KEYS.HOME);

        // setNearByRestaurants();
        checkAndRequestPermissions()

/*
        if(NotificationManagerCompat.from(this).areNotificationsEnabled())
        {
            //Do your Work
        }
        else
        {
//            Snackbar.make(findViewById(R.id.drawer_layout), "Notification blocked", Snackbar.LENGTH_LONG).setAction("Settings") {
//                // Responds to click on the action
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                val uri: Uri = Uri.fromParts("package", packageName, null)
//                intent.data = uri
//                startActivity(intent)
//            }.show()

            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Turn Notifications On").setCancelable(false)
                .setMessage("Turn push notifications on for Halal Advisor to receive new venue alerts and special deals.")
                .setPositiveButton("Settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()

            alertDialog.show()
            //Ask for permission
        }*/






        try {
            val params_restaurantList = Bundle()
            params_restaurantList.putString(
                "UserEmail",
                PrefUtils.getProfileObject(context).useremail
            )
            params_restaurantList.putString("UserType", "Registered")
            mFirebaseAnalytics!!.logEvent("Restaurant_Listing", params_restaurantList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val currentTimestamp: String
        get() {
            var ts = ""
            try {
                val tsLong = System.currentTimeMillis() / 1000
                ts = tsLong.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ts
        }

    private fun hideProgressBar() {
        progressBar!!.visibility = View.GONE
    }

    private fun showProgressBar() {
        progressBar!!.visibility = View.VISIBLE
    }

    val currentMonth: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("MMMM")
            val date = Date()
            Log.d("Month", dateFormat.format(date))
            return dateFormat.format(date)
        }

    private fun trackActivity(activityType: String) {
        apiService!!.trackActivity(
            activityType,
            PrefUtils.getProfileObject(this@SwipeHomeActivity).u_id, currentTimestamp, currentMonth,""
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<AddReviewRes> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(res: AddReviewRes) {}
                override fun onError(e: Throwable) {
                    Log.e("err", " in profile--$e")
                }

                override fun onComplete() {}
            })
    }

    private val featuredImages: Unit
        private get() {
            apiService!!.featuredImages.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<FeaturedImagesRes> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(res: FeaturedImagesRes) {}
                    override fun onError(e: Throwable) {
                        Log.e("err", " in profile--$e")
                    }

                    override fun onComplete() {}
                })
        }

    override fun onResume() {
        super.onResume()
        initUi()
        getLocation()
        setupNavigation()
        nearByRestaurants
       // reqRestaurantListVolley()
       // nearByResNew()
        initFcm()

        // Show bottom layout directly
        foldState = 1
        updateUiForFoldState()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    if (data != null) {
                        val addressObject = Utils.getObjectFromJson(
                            data.getStringExtra("result"),
                            AddressObject::class.java
                        ) as AddressObject
                        try {
                            val locationObject = LastSearchLocationObject()
                            locationObject.address = addressObject.fullAddress
                            /*     Geocoder gcd = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses = gcd.getFromLocation(Double.parseDouble(addressObject.getLatitude())
                                    , Double.parseDouble(addressObject.getLongitude()), 1);
                            if (addresses.size() > 0) {
                                System.out.println(addresses.get(0).getLocality());
                                locationObject.setName(addresses.get(0).getLocality());
                            }
                            else {
                                // do your stuff
                            }*/locationObject.name = addressObject.urban_area
                            locationObject.code = addressObject.state_code
                            locationObject.lattitude = addressObject.latitude.toDouble()
                            locationObject.longitude = addressObject.longitude.toDouble()
                            PrefUtils.saveLastSearchedLocation(context, locationObject)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
                RESULT_CANCELED -> {}
            }


            //=======
        }
    }

    private fun initUi() {
        gestureScanner = GestureDetector(this)
        swipeUpIcon = findViewById(R.id.up_btn)
        rv_nearby = findViewById(R.id.rv_nearby)
        no_record_found = findViewById(R.id.no_record_found)
        linear_nearBy = findViewById(R.id.linear_nearBy)
        rell = findViewById(R.id.rell)
        header = findViewById(R.id.header)
        quick_view = findViewById(R.id.quick_view)
        imageView7 = findViewById(R.id.clickImage)
        user_name = findViewById(R.id.user_name)
        edSearch = findViewById(R.id.searchMoreBtn)
        try {
            val fullName = PrefUtils.getProfileObject(context).username ?: ""
            val firstName = fullName.trim().split("\\s+".toRegex()).firstOrNull() ?: ""

            val formattedName = firstName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }

            user_name?.text = "Salaams, $formattedName 👋"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //        swipeUpIcon.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureScanner.onTouchEvent(event);
//            }
//        });
        drawerLayout = findViewById(R.id.drawer_layout)
        navIcon = findViewById(R.id.nav_icon)
        drawerLayout!!.addDrawerListener(this)
        navigationView = findViewById(R.id.nvView)
        nav_listView = findViewById(R.id.nav_listview)
        apiService = ApiFactory.getRetrofitInstance().create(ApiService::class.java)
        // navHeader = navigationView.getHeaderView(0);
        navIcon!!.setOnClickListener(this)
        edSearch!!.setOnClickListener(this)
        imageView7!!.setOnClickListener(this)

        swipeUpIcon!!.visibility = View.GONE
        quick_view!!.visibility = View.GONE
    }

    fun setView(fragment: Fragment?, keyFragment: Int) {
        val transaction = supportFragmentManager
        transaction.beginTransaction() //.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment!!, "" + keyFragment).commit()
        /* transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.add(R.id.fragment_container, fragment, keyFragment);*/
    }

    private fun openAutoCompletePlaceActivityResult() {
/*
asdasdasd
// Set the fields to specify which types of place data to
// return after the user has made a selection.
        List<Place.Field>
                fields = Arrays.asList(
                Place.Field.ID
                , Place.Field.NAME
                , Place.Field.ADDRESS
                , Place.Field.LAT_LNG);

// Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .setCountry("AU")
                .build(context);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);*/
        val intent = Intent(context, AutoCompleteAddressSearchActivity::class.java)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private fun setupNavigation() {
        tv_navUsername = findViewById(R.id.tv_navigation_username)
        navUserImage = findViewById(R.id.iv_navigation_userImage)
        try {
            tv_navUsername!!.setText(PrefUtils.getProfileObject(context).username)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val imageUrl = PrefUtils.getProfileObject(context).profile_image_url

            if (!imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .noFade()
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(navUserImage)
            } else {
                navUserImage?.setImageResource(R.drawable.ic_profile_placeholder)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        /*Passing List into adpater
         * Setting adapter in listview*/nav_nameId_logout =
            resources.getStringArray(R.array.left_menu_logout)
        nav_nameId_login = resources.getStringArray(R.array.left_menu_login)
        nav_listView!!.adapter = NavigationListAdapter(this, nav_nameId_logout)
        nav_listView!!.overscrollFooter = ColorDrawable(Color.TRANSPARENT)
        nav_listView!!.onItemClickListener = this
        //nav_listView.setOnItemClickListener(this);
    }

    override fun onClick(view: View) {
        if (view === navIcon) {
            drawerLayout!!.openDrawer(Gravity.RIGHT)
        }
        if (view === edSearch) {
            startActivity(Intent(context, MainActivity::class.java))
        }
        if (view === imageView7) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("notification_type", "scanner") // Use existing logic in MainActivity to select tab
            startActivity(intent)
        }
    }

    private fun toggleHeader() {
        foldState = (foldState + 1) % 2
        Log.d("SwipeHomeActivity", "toggleHeader: foldState = $foldState")
        updateUiForFoldState()
    }

    private fun updateUiForFoldState() {
        val transition = AutoTransition()
        transition.duration = 400
        val mainContainer = header?.parent as? ViewGroup
        if (mainContainer != null) {
            TransitionManager.beginDelayedTransition(mainContainer, transition)
        }

        when (foldState) {
            0 -> { // State 0: Full Screen View
                imageView7!!.visibility = View.VISIBLE
                user_name!!.visibility = View.VISIBLE
                header!!.background = null
                header!!.translationY = 0f
                header!!.setPadding(header!!.paddingLeft, header!!.paddingTop, header!!.paddingRight, 0)

                quick_view!!.visibility = View.VISIBLE
                swipeUpIcon!!.visibility = View.VISIBLE
                swipeUpIcon!!.setImageDrawable(resources.getDrawable(R.drawable.ic_swipe_down))

                if (isListVisible) {
                    onSwipeBottom()
                }
            }
            1 -> { // State 1: Folded View (Header pushed up, Bottom Layout folds in)
                imageView7!!.visibility = View.VISIBLE
                user_name!!.visibility = View.VISIBLE
                header!!.background = null
                // Move header slightly up to create "fold" effect
                header!!.translationY = -dpToPx(10f, this).toFloat()
                header!!.setPadding(header!!.paddingLeft, header!!.paddingTop, header!!.paddingRight, dpToPx(30f, this))

                quick_view!!.visibility = View.GONE
                swipeUpIcon!!.visibility = View.GONE

                if (!isListVisible) {
                    onSwipeTop()
                }
            }
        }
    }

    override fun onItemClick(adapterView: AdapterView<*>?, view: View, position: Int, l: Long) {
        try {
            drawerLayout!!.closeDrawer(Gravity.RIGHT)
            DRAWER_ACTION = position
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
    override fun onDrawerOpened(drawerView: View) {
        DRAWER_ACTION = -1
    }

    private fun initFcm() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task -> // device_token = task.getResult();
                     val token = task.result
            LogUtils.printf("I am Token success getInstanceId failed$token")
            PrefUtils.saveFirebaseToken(context, token)
        }
    }

    override fun onDrawerClosed(drawerView: View) {
        try {
            if (DRAWER_ACTION == DRAWER_ACTION_LOG_OUT) {
                val alertDialogManager = AlertDialogManager()
                alertDialogManager.setLogoutDelegate { isLogoutOk ->
                    if (isLogoutOk) {
                        ReqLogOut().execute()
                        //logOutFunction();
                    }
                }
                AlertDialogManager.showOkCancelAlertDialog(
                    context,
                    "Logout",
                    "Do you really want to logout?"
                )
            } else if (DRAWER_ACTION == DRAWER_ACTION_SCAN_GROCERIES) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("notification_type", "scanner")
                startActivity(intent)
            } else if (DRAWER_ACTION == DRAWER_ACTION_OFFER) {
                startActivity(Intent(context, VoucharActivity::class.java))
            } else if (DRAWER_ACTION == DRAWER_ACTION_RATE_US) {
                openPlayStore()
            } else if (DRAWER_ACTION == DRAWER_ACTION_SEND_FEEDBACK) {
                startActivity(Intent(context, SendFeedbackActivity::class.java))
            } else if (DRAWER_ACTION == DRAWER_ACTION_HOME) {
                startActivity(Intent(context, MainActivity::class.java))
            } else if (DRAWER_ACTION == DRAWER_ACTION_MY_FAVOURITES) {
                startActivity(Intent(context, FavouritesActivity::class.java))
            } else if (DRAWER_ACTION == DRAWER_ACTION_ADVERTISE_ON_HALAL_APP) {
                startActivity(
                    Intent(context, AboutWebScreen::class.java).putExtra(
                        "about",
                        "https://halaladvisor.com.au/restaurant-discovery/"
                    )
                        .putExtra("type", "ADVERTISE WITH HALAL ADVISOR")
                )
            } else if (DRAWER_ACTION == DRAWER_ACTION_ABOUT_HALAL_APP) {
                startActivity(
                    Intent(context, AboutWebScreen::class.java).putExtra(
                        "about",
                        "https://halaladvisor.com.au/about-halal-advisor"
                    )
                        .putExtra("type", "About Halal Advisor")
                )
            } else if (DRAWER_ACTION == DRAWER_ACTION_PROFILE) {
                startActivity(Intent(context, ProfileActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDrawerStateChanged(newState: Int) {}
    private fun shareIt() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, AllConstants.ShareName)
        sharingIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Download the Halal Advisor App -" + " https://play.google.com/store/apps/details?id=com.dapl.halaladvisor"
        )
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    fun openBrowser(link: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    }

    fun openPlayStore() {
        val appPackageName = "com.dapl.halaladvisor" // from Context or Activity object
        try {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            startActivity(intent)
        } catch (anfe: ActivityNotFoundException) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun setUpGoogleApiClientInstance() {
        mGoogleApiClient = GoogleApiClient.Builder(context!!)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(Plus.API)
            .addScope(Plus.SCOPE_PLUS_LOGIN)
            .build()
        mGoogleApiClient!!.connect()
    }

    private fun signOutGoogle() {
        mGoogleApiClient?.let {
            Auth.GoogleSignInApi.signOut(it).setResultCallback(
                object : ResultCallback<Status> {
                    override fun onResult(status: Status) {
                        Log.e("TANAY", "signed out from google")
                    }
                })
        }
    }

    private fun signOutFromGoogle() {
        if (mGoogleApiClient!!.isConnected) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient!!)
            mGoogleApiClient!!.disconnect()
            mGoogleApiClient!!.connect()
        }


        /* Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        mGoogleApiClient.disconnect();*/
    }

    private fun revokeAccess() {
        mGoogleApiClient?.let {
            Auth.GoogleSignInApi.revokeAccess(it).setResultCallback(
                object : ResultCallback<Status> {
                    override fun onResult(status: Status) {
                        Log.e("TANAY", "revoked access ot from google")
                    }
                })
        }
    }

    override fun onConnected(bundle: Bundle?) {
        try {
            mGoogleApiClient!!.clearDefaultAccountAndReconnect()
            Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
                .setResultCallback(object : ResultCallback<LoadPeopleResult> {
                    override fun onResult(loadPeopleResult: LoadPeopleResult) {
                        Log.e("TANAY", "onResult")
                        // signOutFromGoogle();
                        //  signOutGoogle();
                        revokeAccess()
                    }
                })
            signOutFromGoogle()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureScanner?.onTouchEvent(event) ?: super.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        var result = false

        try {
            if (e1 == null) return false

            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x

            if (Math.abs(diffY) > Math.abs(diffX) &&
                Math.abs(diffY) > SWIPE_THRESHOLD &&
                Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (diffY > 0) {
                    if (isListVisible) onSwipeBottom()
                } else {
                    if (!isListVisible) onSwipeTop()
                }
                result = true
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }


    override fun onLongPress(e: MotionEvent) {}
    override fun onScroll(
        p0: MotionEvent?,
        p1: MotionEvent,
        p2: Float,
        p3: Float
    ): Boolean {
        return false
    }


    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    private inner class ReqLogOut : AsyncTask<Void?, Void?, String?>() {
        var response: String? = null
        var progressDialog: ProgressDialog? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage("Please Wait...")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }

        protected override fun doInBackground(vararg voids: Void?): String? {
            val params = HashMap<String, String>()
            params["u_id"] = PrefUtils.getProfileObject(context).u_id
            params["device_type"] = "A"
            params["device_token"] = PrefUtils.getFirebaseToken(context)
            LogUtils.printf("I am Token: " + PrefUtils.getFirebaseToken(applicationContext))
            val parser = JSONParser()
            val mURL = AppData.LOGOUT_URL
            LogUtils.printf("I am url: $mURL")
            response = parser.makeHttpRequest(mURL, "POST", params)
            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            progressDialog!!.dismiss()
            signOutGoogle()
            LogUtils.printf("I am LogOut response: $response")
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.optInt("status") == 1) {
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT)
                        .show()
                    if (FacebookSdk.isInitialized()) {
                        if (LoginManager.getInstance() != null) {
                            LoginManager.getInstance().logOut()
                        }
                    } else {
                        FacebookSdk.sdkInitialize(applicationContext)
                        if (LoginManager.getInstance() != null) {
                            LoginManager.getInstance().logOut()
                        }
                    }
                    setUpGoogleApiClientInstance()
                    PrefUtils.removeAllSavedData(context)
                    startActivity(Intent(context, LogInActivity::class.java))
                    finish()
                } else {
                    AlertDialogManager.showAlertDialog(
                        context,
                        "Alert",
                        jsonObject.optString("message"),
                        false,
                        false
                    )
                }


                //   Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onSwipeTop() {
        //  Toast.makeText(context, "Swiped Up", Toast.LENGTH_SHORT).show();
        viewVisibleAnimator(linear_nearBy)
        swipeUpIcon!!.setImageDrawable(resources.getDrawable(R.drawable.ic_swipe_up))
        isListVisible = true
    }

    fun onSwipeBottom() {
        //  Toast.makeText(context, "Swiped Down", Toast.LENGTH_SHORT).show();
        //  rv_nearby.setVisibility(View.GONE);
//        rv_nearby.animate()
//                .translationY(0)
//                .alpha(0.0f)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        rv_nearby.setVisibility(View.GONE);
//                    }
//                });
        viewGoneAnimator(linear_nearBy)
        swipeUpIcon!!.setImageDrawable(resources.getDrawable(R.drawable.ic_swipe_down))
        isListVisible = false
    }

    private fun viewGoneAnimator(view: View?) {
//        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.slide_down);

//        view.animate()
//                .alpha(1f)
//                .setDuration(300)
//                .setStartDelay(1000)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        view.setVisibility(View.GONE);
//                    }
//                });
        setNearByRestaurants()
        val anim = ValueAnimator.ofInt(rell!!.measuredHeight, 0)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = rell!!.layoutParams
            layoutParams.height = `val`
            rell!!.layoutParams = layoutParams
            if (newNearByRestaurantAdapter != null) {
                newNearByRestaurantAdapter!!.notifyDataSetChanged()
            }
        }
        anim.duration = 600
        anim.start()
    }

    private fun viewVisibleAnimator(view: View?) {

//        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.slide_up);
//
//        view.animate()
//                .translationY(view.getHeight())
//
//                .alpha(1.0f)
//                .setDuration(300)
//                .setStartDelay(1000)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        view.setVisibility(View.VISIBLE);
//                    }
//                });
        val dp = this.resources.getDimensionPixelSize(R.dimen._260sdp)
        val anim = ValueAnimator.ofInt(rell!!.measuredHeight, dp)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = rell!!.layoutParams
            layoutParams.height = `val`
            rell!!.layoutParams = layoutParams
            if (newNearByRestaurantAdapter != null) {
                newNearByRestaurantAdapter!!.notifyDataSetChanged()
            }
        }
        anim.duration = 600
        anim.start()
    }

    val dateTime: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val cal = Calendar.getInstance()
            return dateFormat.format(cal.time)
        }

    //  latitude = -33.86714 ;
    //  longitude =  151.2091087 ;
    //        "-33.86714","151.2091087"
    val nearByRestaurants: Unit
        get() {
            //  latitude = -33.86714 ;
            //  longitude =  151.2091087 ;
            AllConstants.UPlat = latitude.toString()
            AllConstants.UPlng = longitude.toString()
           // Toast.makeText(context,latitude.toString(),Toast.LENGTH_SHORT).show()
            showProgressBar()

            val datee = dateTime.split(" ").toTypedArray()

            //        "-33.86714","151.2091087"
            apiService!!.getNearByRestaurants(
                latitude, longitude, datee[0], datee[1], PrefUtils.getProfileObject(context).u_id
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<NearbyRestaurantResponse> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(res: NearbyRestaurantResponse) {
                        hideProgressBar()
                        if (res.status == "1") {
                            rv_nearby!!.visibility = View.VISIBLE
                            no_record_found!!.visibility = View.GONE
                            nearbyRest.clear()
                            nearbyRest.addAll(res.result)
                            setNearByRestaurants()
                            Log.d("dateedatee", ": "+latitude+"  "+ longitude)
                            Log.d("dateedatee", ": "+datee[0]+"  "+  datee[1])
                            Log.d("dateedatee", ": "+PrefUtils.getProfileObject(context).u_id)
                        } else {
                            rv_nearby!!.visibility = View.GONE
                            no_record_found!!.visibility = View.VISIBLE
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.e("err", " in profile--$e")
                        hideProgressBar()
                    }

                    override fun onComplete() {}
                })
        }
fun nearByResNew()
{
    AllConstants.UPlat = latitude.toString()
    AllConstants.UPlng = longitude.toString()
   // Toast.makeText(context,latitude.toString(),Toast.LENGTH_SHORT).show()
    showProgressBar()
    val datee = dateTime.split(" ").toTypedArray()
    //        "-33.86714","151.2091087"
    apiService!!.getNearByRestaurants(
        latitude, longitude, datee[0], datee[1], PrefUtils.getProfileObject(context).u_id
    ).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(object : Observer<NearbyRestaurantResponse> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(res: NearbyRestaurantResponse) {
                hideProgressBar()

                if (res.status == "1") {
                    rv_nearby!!.visibility = View.VISIBLE
                    no_record_found!!.visibility = View.GONE
                    nearbyRest.clear()
                    nearbyRest.addAll(res.result)
                    setNearByRestaurants()
                } else {
                    rv_nearby!!.visibility = View.GONE
                    no_record_found!!.visibility = View.VISIBLE
                }
            }

            override fun onError(e: Throwable) {
                Log.e("err", " in profile--$e")
                hideProgressBar()
            }

            override fun onComplete() {}
        })
}

    open fun reqRestaurantListVolley() {
        showProgressBar()
        //hideLayoutMessage();
        AppController.getInstance().requestQueue.cancelAll( TAG_REQUEST_RESTAURANT_SEARCH)
        val baseUrl = AppData.RESTAURANT_LIST_URL
        LogUtils.printf("I am Restaurant List baseUrl: $baseUrl")
        /*if (restaurentList.size() > 0) {
            hideProgressBar();
        } else {
            showProgressBar();
        }*/


        //showProgressBar();
        val strReq: StringRequest = object : StringRequest(
            Method.POST, baseUrl,
            Response.Listener<String> { response ->
                hideProgressBar()
                try {
                    LogUtils.printf("I am Restaurant List response: $response")
                    val jsonObject = JSONObject(response)
                    // restaurentList.clear();
                    //   latlngs.clear();
                    var `res`: NearbyRestaurantResponse? = null
                    if (jsonObject != null) {
                        val gson = Gson()
                        `res` = gson.fromJson(jsonObject.toString(), NearbyRestaurantResponse::class.java)
                    }
                    hideProgressBar()

                    if (res!!.status == "1") {
                        rv_nearby!!.visibility = View.VISIBLE
                        no_record_found!!.visibility = View.GONE
                        nearbyRest.clear()
                        nearbyRest.addAll(res.result)
                        setNearByRestaurants()
                    } else {
                        rv_nearby!!.visibility = View.GONE
                        no_record_found!!.visibility = View.VISIBLE
                    }

                    //setData(response)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                LogUtils.printf("I am error: " + error.message)
                hideProgressBar()
                //hideProgressBar();
                try {

                    /*showLayoutMessage();
                          ivMessage.setImageDrawable(getResources().getDrawable(R.drawable.ic_net_conn_fail));
                          String msg1 = "Server not respond. <u><font color='#202c5d'>Click to Retry</font></u>";
                          tvMessage.setText(Html.fromHtml(msg1));*/
                    // Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }) {
            override fun getParams(): Map<String, String> {
                AllConstants.UPlat = latitude.toString()
                AllConstants.UPlng = longitude.toString()

                val datee = dateTime.split(" ").toTypedArray()
                //        "-33.86714","151.2091087"

                    val params: MutableMap<String, String> = HashMap()
                params["lattitude"] = latitude.toString()
                params["longitude"] = longitude.toString()
                //params.put("lattitude", "-33.7246958");
                //params.put("longitude", "150.8408682");
                params["presentdate"] =  datee[0]
                params["time"] = datee[1]
                params["u_id"] = PrefUtils.getProfileObject(context).u_id

                return params
            }
        }
        Utils.setRetryPolicy(strReq)
        AppController.getInstance()
            .addToRequestQueue(strReq,  TAG_REQUEST_RESTAURANT_SEARCH)

}
    fun getLocation() {
        try {
            locationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager

            //getting GPS status
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            //getting network status
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                isGPSTrackingEnabled = true
                Log.d(TAG, "Application use GPS Service")

                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */provider_info = LocationManager.GPS_PROVIDER
            } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
                isGPSTrackingEnabled = true
                Log.d(TAG, "Application use Network State to get GPS coordinates")

                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */provider_info = LocationManager.NETWORK_PROVIDER
            }

            // Application can use GPS or Network Provider
            if (!provider_info!!.isEmpty()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    showSettingsAlert()
                    return
                }
                locationManager!!.requestLocationUpdates(
                    provider_info!!,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this
                )
                if (locationManager != null) {
                    location = lastKnownLocation
                    updateGPSCoordinates()
                }
            }
        } catch (e: Exception) {
            //e.printStackTrace();
            Log.e(TAG, "Impossible to connect to LocationManager", e)
        }
    }// Found best last known location: %s", l);

    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    private val lastKnownLocation: Location?
        private get() {
            locationManager =
                applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            val providers = locationManager!!.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return null
                }
                val l = locationManager!!.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    // Found best last known location: %s", l);
                    bestLocation = l
                }
            }
            return bestLocation
        }

    fun updateGPSCoordinates() {
        if (location != null) {
            latitude = location!!.latitude
            longitude = location!!.longitude
        }
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(context)

        //Setting Dialog Title
        alertDialog.setTitle("Enable Location Permission")

        //Setting Dialog Message
        alertDialog.setMessage("Your location permission is turned off, Kindly turn on the permissions!")

        //On Pressing Setting button
        alertDialog.setPositiveButton(R.string.action_settings) { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context!!.startActivity(intent)
        }

        //On pressing cancel button
        alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    fun setNearByRestaurants() {
        rv_nearby!!.visibility = View.VISIBLE
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_nearby!!.layoutManager = linearLayoutManager
        newNearByRestaurantAdapter = NewNearByRestaurantAdapter(context, nearbyRest)
        rv_nearby!!.adapter = newNearByRestaurantAdapter
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
        private const val TAG = "tag"

        // The minimum distance to change updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1 // 1 minute
                ).toLong()

        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }
    }

    private fun checkAndRequestPermissions() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // This is called if the user has denied the permission before.
                // In this case, we are just asking the permission again.
                ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
            }
        } else {
            // The permission is already granted.
            // startLocationUpdates()
           // Toast.makeText(this, "$permission is already granted.", Toast.LENGTH_SHORT).show()
        }
    }


}