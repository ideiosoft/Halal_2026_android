package com.dapl.halaladvisor.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.dapl.halaladvisor.Adapter.InstaAdapter
import com.dapl.halaladvisor.Adapter.InstaViewPagerAdapter
import com.dapl.halaladvisor.Model.featuredimages.Datum
import com.dapl.halaladvisor.Model.featuredimages.FeaturedImagesRes
import com.dapl.halaladvisor.Model.instanewres.EdgeXX
import com.dapl.halaladvisor.Model.instanewres.InstaNewRes
import com.dapl.halaladvisor.Model.instaresfinal.Data
import com.dapl.halaladvisor.Model.instaresfinal.InstaResFinal
import com.dapl.halaladvisor.R
import com.dapl.halaladvisor.Utility.InstagramApp
import com.dapl.halaladvisor.extra.ApiFactory
import com.dapl.halaladvisor.extra.ApiService
import com.dapl.halaladvisor.interfaces.OnResponseInstaListner
import com.github.fajaragungpramana.dotsindicator.DotsIndicator
import com.google.gson.GsonBuilder
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class InstaExplorePage : Fragment(), OnResponseInstaListner {
    var featuredImages: ArrayList<Datum> = ArrayList()
    lateinit var rvImages: RecyclerView
    lateinit var apiService: ApiService
    lateinit var pager: ViewPager
    lateinit var instaAdapter: InstaAdapter
    lateinit var dotsIndicator: DotsIndicator
    var rid = ""
    var contextm: Context? = null
    private var instaObj: InstagramApp? = null

    //var arr_InstaImages_temp: List<com.dapl.halaladvisor.Model.instares.Datum> = ArrayList()
    var arr_InstaImages_temp: List<Data> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_insta_explore_page, container, false)
        initUi(v)


       // fetchInstaPhotos()

//        getInstaImages(
//            "IGQVJXOHJLcTFaQy14ZAHR0V3JUMWpQaTlKMWJacVhXNjdEdjVBMEFUc0Y4LTJTNF9jYVI3SGZABaW80WEtVR0tLczFIUGNxazdZAd1BqYXVlQmdncXBldDNieGFDb0xfak16QWVaSU1n",
//            ""
//        )
//        getInstaImages(
//            "IGQWRQbE95V29JREYzLWl1alJmeDBnYmwyN3MwdjhyQ21IWDM1MWF5LTNLNUlfT2ZAoV2RyY00zN0dZAUy1MRGdzZAnFoR1pRMUJwb2RWV1NPc2FtOHhaX3UxQWxBdmlXX1ZAUZAExTUS1zamF1UQZDZD",
//            ""
//        )
//
        getInstaImages(
            "IGQWROcXdhOFBVUkJQWVRzQzVXQ0ZACQ0Y5S1hpU2NjOGlQaFpXZAFRmejRCRU5wcTl0UjdwV3dCSjQzZA180cms5S2VMVHpSSkthSjZAUNk9NVEMtUGNLT3JXaWs5NkZAKd2RXSDNGbFd2dmNkQQZDZD",
            ""
        )

        getFeaturedImages()
        return v
    }


    fun initUi(v: View) {
        rvImages = v.findViewById(R.id.rvInsta)
        pager = v.findViewById(R.id.pager)

        dotsIndicator = v.findViewById(R.id.dots_indicator)

        getFeaturedImages()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contextm = context
    }

    fun setViewPagerAdapter(list: ArrayList<Datum>, baseurl: String) {
        val viewPagerAdapter: InstaViewPagerAdapter = InstaViewPagerAdapter(list, contextm, baseurl)

        pager.setAdapter(viewPagerAdapter)
        pager.setCurrentItem(0)
        dotsIndicator.viewPager = pager


    }

/*    fun setInstaAdapter() {
        *//*
            if (imagesList.size() !== 0) {*//*
        val galleryAdapter = InstaAdapter(context *//*this, imagesList*//*)
        val linearLayoutManager: LinearLayoutManager = GridLayoutManager(context, 3)
        rvImages.setLayoutManager(linearLayoutManager)
        rvImages.setAdapter(galleryAdapter)
        *//*  } else {
              tvNoRecord.setVisibility(View.VISIBLE)
              rvGallery.setVisibility(View.GONE)
          }*//*
    }*/

    fun newInstance(): Fragment? {
        return FavouriteFragment()
    }

    fun fetchInstaPhotos() {
        instaObj = InstagramApp(
            requireContext(), getString(R.string.app_id),
            getString(R.string.app_secret), getString(R.string.redirect_url)
        )


        val listener = object : InstagramApp.OAuthAuthenticationListener {

            override fun onSuccess() {


                //  getInstaImages("IGQVJXNldCaTNIT2djSXZAkbEQ5NFU2M095NXN5VTAxY0g1aC1uVS1TZAUg0NEtkemZAxRUdKbEVxUzVKUmo1X1hZAOTltWW9qeVd3Y0E3a2JETnFUc2J5MjNGcElXcUZAGclJ3VUQ2MzdXLU1rYWJsek9BTHl3SWlfSEpBQnFv",instaObj!!.id)

                Log.e("Name", instaObj!!.getAccessToken())
                Log.e("UserName", instaObj!!.getId())
                if (instaObj!!.getAccessToken() != null) {
                    Log.e("dfdfdf", "dfdsd")


                    getRefreshAccessToken(instaObj!!.getAccessToken(), "")


//fetchUserName(instaObj.getAccessToken());
                    val urll =
                        "https://graph.instagram.com/me/media?fields=id,media_type,media_url,thumbnail_url,timestamp,username,permalink,caption&limit=1000&access_token=" + instaObj!!.getAccessToken()
                    //GetApiCall(context, "", urll).execute()
                    /* Server(
                             ApiService.BASE_URL + ApiService.MY_DATA + "", this@InstaExplorePage, context, "").getInstaPosts(urll)
 */


                }
            }

            override fun onFail(error: String) {

                Log.e("error", "---" + error)
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        instaObj!!.setListener(listener)
        instaObj!!.authorize("show")
    }

    override fun onInstaResponse(jsonObject: JSONObject) {

        Log.e("insta response--", "--" + jsonObject)
        for (i in 0 until jsonObject.getJSONArray("data").length()) {
            val item = jsonObject.getJSONArray("data").getJSONObject(i)
            if (item.getString("media_type") == "IMAGE") {
                // arr_InstaImages_temp.add(item.getString("media_url"))


                val gson = GsonBuilder().create()
                //  arr_InstaImages_temp   = gson.fromJson(jsonObject.getJSONArray("data").toString() , Array<com.dapl.halaladvisor.Model.instares.Datum>::class.java).toList()


                //                myPhotos!!.add(AddBestPhotoModel(item.getString("media_url"), 1, 3))
                //                instaPhotos.add(item.getString("media_url"))
            }

        }

        /*  val linearLayoutManager: LinearLayoutManager = GridLayoutManager(context, 3)
          rvImages.layoutManager = linearLayoutManager
          instaAdapter = InstaAdapter(context, arr_InstaImages_temp)

          rvImages.adapter = instaAdapter*/
    }

    override fun onInstaError(errorString: String) {

        Log.e("insta errorString--", "--" + errorString)
    }


    private fun getFeaturedImages() {
        apiService = ApiFactory.getRetrofitInstance().create(ApiService::class.java)
        apiService.getFeaturedImages().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<FeaturedImagesRes> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.e("err", " in profile--$e")
                }

                override fun onComplete() {}
                override fun onNext(t: FeaturedImagesRes) {


                    featuredImages.clear()
                    featuredImages.addAll(t.data)
                    setViewPagerAdapter(featuredImages, t.baseUrl)
                }
            })


    }


    fun setInstaNewAdapter() {
        val linearLayoutManager: LinearLayoutManager = GridLayoutManager(context, 3)
        rvImages.layoutManager = linearLayoutManager
        instaAdapter = InstaAdapter(context, arr_InstaImages_temp)

        rvImages.adapter = instaAdapter
    }

    private fun getInstaImages(accesstoken: String, id: String) {


        apiService = ApiFactory.getRetrofitInstance().create(ApiService::class.java)
        apiService.getHalalProfile("https://graph.instagram.com/" + "17841403879853603" + "/media?fields=id,media_type,media_url,thumbnail_url,timestamp,username,permalink,caption&limit=1000&access_token=" + accesstoken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<InstaResFinal> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.e("err", " in insta--$e")
                }

                override fun onComplete() {}
                override fun onNext(t: InstaResFinal) {
                    Log.e("isnta res", " in insta--$t")
                    arr_InstaImages_temp = t.data
                    setInstaNewAdapter()
                }
            })

    }

    private fun getRefreshAccessToken(accesstoken: String, id: String) {


        apiService = ApiFactory.getRetrofitInstance().create(ApiService::class.java)
        apiService.getRefreshAccessToken("https://graph.instagram.com/access_token?grant_type=ig_exchange_token&client_secret=e4ba9d94b37be4457ee3b77d958bfee7&access_token=" + accesstoken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<InstaResFinal> {
                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {
                    Log.e("err", " in insta--$e")
                }

                override fun onComplete() {}
                override fun onNext(t: InstaResFinal) {
                    Log.e("isnta res long live", " in insta--$")

                }
            })

    }
}


