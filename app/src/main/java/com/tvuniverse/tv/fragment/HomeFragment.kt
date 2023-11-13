package com.tvuniverse.tv.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.tvuniverse.tv.activity.*
import com.tvuniverse.tv.model.HomeModel
import com.tvuniverse.tv.presenter.CardPresenter
import com.tvuniverse.tv.singleton.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BrowseSupportFragment() {
    private var rowAdapter: ArrayObjectAdapter? = null
    private var cardPresenterHeader: HeaderItem? = null
    private var cardRowAdapter: ArrayObjectAdapter? = null
    val timeZone = TimeZone.getDefault()
    var start_date:String=""
    var end_date:String=""



    companion object {
        var startDateAfterCon_new: String = ""
    }

//    private var cardPresenter = CardPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUIElements()
    }

    private fun setupUIElements() {
        headersState = HEADERS_DISABLED
        isHeadersTransitionOnBackEnabled = true
        setUpEvents()

    }

    fun loadData() {
        var listRowPresenter = ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false)
        cardPresenterHeader = HeaderItem("Live Events")
        var cardPresenter = CardPresenter()
        cardRowAdapter = ArrayObjectAdapter(cardPresenter)
        rowAdapter = ArrayObjectAdapter(listRowPresenter)
        val stringRequest: StringRequest = @SuppressLint("NotifyDataSetChanged")
        object : StringRequest(
            Method.GET, "https://aoswhy2hq2.execute-api.us-east-1.amazonaws.com/app/liveevent",
            Response.Listener { response ->
                try {

                    val jsonObject = JSONObject(response)
                    Log.d("TTT", "HomeFragment :$response")

                    val jsonArray = jsonObject.getJSONArray("data")
                    if (jsonArray.length() > 0) {
                        for (i in 0 until jsonArray.length()) {
                            val homeModel = HomeModel()
                            val jsonObjectItems = jsonArray.getJSONObject(i)
                            homeModel.title = jsonObjectItems.getString("title")
                            homeModel._id = jsonObjectItems.getString("_id")
                            homeModel.type = "live"
                            homeModel.description = jsonObjectItems.getString("description")
                            homeModel.thumbnail = jsonObjectItems.getString("thumbnail")
                            homeModel.status = jsonObjectItems.getString("status")
                            homeModel.backgroundImage =
                                jsonObjectItems.getString("background_image")
                            start_date=jsonObjectItems.getString("start_date")
                            homeModel.startDate = jsonObjectItems.getString("start_date")
                            homeModel.status = jsonObjectItems.getString("status")
                            homeModel.endDate = jsonObjectItems.getString("end_date")
                            end_date=jsonObjectItems.getString("end_date")

                            homeModel.rtmp_endpoint = jsonObjectItems.getString("rtmp_endpoint")
                            homeModel.stream_key = jsonObjectItems.getString("stream_key")
                            homeModel.playbackURL = jsonObjectItems.getString("playbackURL")
                            Log.d("zzz","PlaybackUrl"+jsonObjectItems.getString("playbackURL"))
                            cardRowAdapter!!.add(homeModel)
                        }
                        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
                        val startDate: Date = parser.parse(start_date)
                        val endDate: Date = parser.parse(end_date)
                        val currentDate = Date()

                        if (currentDate.before(endDate)) {
                            rowAdapter!!.add(ListRow(cardPresenterHeader, cardRowAdapter))
                            loadHomeData()

                        }
                        else {
                            loadHomeData()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
//                    splashProgress.visibility= View.GONE
                }
            },
            Response.ErrorListener {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                HomeActivity.progress_home.visibility = View.GONE
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                return headers
            }
        }
        stringRequest.setShouldCache(false)
        requireActivity().applicationContext.let {
            VolleySingleton.getInstance(it).addToRequestQueue(stringRequest)
        }
    }

    fun loadHomeData() {
        val stringRequest: StringRequest = @SuppressLint("NotifyDataSetChanged")
        object : StringRequest(
            Method.GET, "https://aoswhy2hq2.execute-api.us-east-1.amazonaws.com/app",
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("TTT", "loadHomeData 222 :$response")
                    val jsonArray = jsonObject.getJSONArray("data")

                    for (i in 0 until jsonArray.length()) {
                        var cardPresenter = CardPresenter()
                        cardRowAdapter = ArrayObjectAdapter(cardPresenter)

                        val jsonObject2 = jsonArray.getJSONObject(i)
                        val jsonArrayContent = jsonObject2.getJSONArray("content")
                        for (j in 0 until jsonArrayContent.length()) {
                            cardPresenterHeader = HeaderItem(jsonObject2.getString("title"))
                            val jsonObjectItems = jsonArrayContent.getJSONObject(j)
                            val homeModel = HomeModel()
                            homeModel.title = jsonObjectItems.getString("title")
                            Log.d("zzz", "Title:   " + jsonObjectItems.getString("title"))
                            homeModel._id = jsonObjectItems.getString("_id")
//                            homeModel.buy_price = jsonObjectItems.getString("buy_price")
//                            homeModel.contentState = jsonObjectItems.getString("contentState")
                            homeModel.category = jsonObjectItems.getString("category")
                            homeModel.createdAt = jsonObjectItems.getString("createdAt")
                            homeModel.description = jsonObjectItems.getString("description")
//                            homeModel.enable_in_web = jsonObjectItems.getString("enable_in_web")
//                            homeModel.is_for_buy = jsonObjectItems.getString("is_for_buy")
//                            homeModel.is_for_rent = jsonObjectItems.getString("is_for_rent")
//                            homeModel.subscription = jsonObjectItems.getString("subscription")
                            homeModel.thumbnail = jsonObjectItems.getString("thumbnail")
                            if (jsonObjectItems.has("videoUrl")) {
                                homeModel.videoUrl = jsonObjectItems.getString("videoUrl")
                            }
//                            Log.d("zzz","VideoUrl:   "+jsonObjectItems.getString("videoUrl"))
                            homeModel.type = jsonObjectItems.getString("type")
                            cardRowAdapter!!.add(homeModel)
                        }
                        rowAdapter!!.add(ListRow(cardPresenterHeader, cardRowAdapter))
                    }
                    adapter = rowAdapter
                    HomeActivity.progress_home.visibility = View.GONE
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                HomeActivity.progress_home.visibility = View.GONE
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                return headers
            }
        }
        stringRequest.setShouldCache(false)
        requireActivity().applicationContext.let {
            VolleySingleton.getInstance(it).addToRequestQueue(stringRequest)
        }
    }

    fun setUpEvents() {
        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                if (item is HomeModel) {
                    val content = item

                    if (content.type == "live") {

                        checkCondition(
                            content.startDate,
                            content.endDate,
                            content.title,
                            content.playbackURL,
                            content.thumbnail,
                            content.backgroundImage,content._id
                        )
//                        val intent = Intent(activity, LiveEventActivity::class.java)
//                        intent.putExtra("videoUrl", content.videoUrl)
//                        intent.putExtra("thumb", content.thumbnail)
//                        startActivity(intent)
                    }
                    else if(content.type=="series") {
                        val intent = Intent(activity, SeriesActivity::class.java)
                        intent.putExtra("id", content._id)
                        Log.d("zzz","Series id:  "+content._id)
                        startActivity(intent)
                    }
                    else {
                        val intent = Intent(activity, PlayerActivity::class.java)
                        intent.putExtra("videoUrl", content.videoUrl)
                        intent.putExtra("id", content._id)
                        intent.putExtra("thumb", content.thumbnail)
                        startActivity(intent)
                    }
                }
            }
        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is HomeModel) {
                val content = item
//                HomeActivity.topRow=item.row
//                HomeActivity.tvTitle.setText(content.title)
//                HomeActivity.tvDesc.setText(content.shortDescription)
                Glide.with(itemViewHolder.view.context)
                    .load(content.getThumbnail())
                    .into(HomeActivity.playerThumb)

                HomeActivity.textTitle.setText(content.title)
                HomeActivity.textDescription.setText(content.description)

                if (content.type.equals("live")) {
                    HomeActivity.playerThumb.visibility = View.VISIBLE
//                    (activity as HomeActivity?)!!.playVideo(content.playbackURL)
                    Log.d("zzz", "VideoUrl:   " + content.playbackURL)
                } else {
                    (activity as HomeActivity?)!!.stopPlayer()

                }
                if (content.type.equals("vod")) {
                    HomeActivity.playerThumb.visibility = View.VISIBLE
                    (activity as HomeActivity?)!!.playVideo(content.videoUrl)
                } else {
                    (activity as HomeActivity?)!!.stopPlayer()

                }


            }

        }


    }


    @SuppressLint("NewApi")
    fun checkCondition(
        startDate: String,
        endDate: String,
        title: String,
        videoUrl: String,
        thumb: String,
        backgroundImage: String,
        id: String
    ) {
        Log.d(
            "zzz",
            "Check condition called:  " + "start Date:  " + startDate + "endDate:  " + endDate
        )
//        timeZone.id


        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")

        if (!endDate.isNullOrEmpty() && !startDate.isNullOrEmpty()) {
//            Log.d("zzz", "Check condition called:  1111")
//            val start_Date: Date? = parser.parse(startDate)
//            val end_Date: Date? = parser.parse(endDate)
//            val currentDate = Date()
//
//            val s: Instant = parse(startDate)
//            val e: Instant = parse(endDate)
//            ZoneId.of(timeZone.id)
//            var srartDateAfterCon: LocalDateTime =
//                LocalDateTime.ofInstant(s, ZoneId.of(timeZone.id))
//            System.out.println(srartDateAfterCon)
//
//            var endDateAfterCon: LocalDateTime = LocalDateTime.ofInstant(e, ZoneId.of(timeZone.id))
//            System.out.println(endDateAfterCon)


//            val timeStampFormat = SimpleDateFormat("dd-yyyy-MM'T'HH:mm")
//            val GetDate = Date()
//            val DateStr = timeStampFormat.format(GetDate)
            startDateAfterCon_new = startDate

            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")

            val result = startDate.substring(0, 22) + ":" + startDate.substring(24)
            val end_result = endDate.substring(0, 22) + ":" + endDate.substring(24)



            val start_Date: Date = parser.parse(result)
            val end_Date: Date = parser.parse(end_result)
            
            val currentDate = Date()
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val s: String = df.format(currentDate)

            val new_start_Date: Date = parser.parse(s)

            val new_end_Date: Date = parser.parse(end_result)



//
//            val cdate: Date = parser.parse(currentDate.toString())
            Log.d("zzz","currentDate.after(start_Date) "+ start_Date+"   Curren date:  "+currentDate+ "    S "+ s+"   new_end_Date   "+ new_end_Date)

            Log.d("yyy","new Start Date "+ new_start_Date+"   end Date:  "+new_end_Date)


            Log.d("yyy","Current date "+ currentDate+"   Start Date:  "+ new_start_Date+"   end Date:  "+new_end_Date)



            if (currentDate.after(new_start_Date)) {
                // Show countdown
                val intent = Intent(activity, CountdownActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("id", id)
                intent.putExtra("backGroundImage", backgroundImage)
                intent.putExtra("date", startDate)
                intent.putExtra("videoUrl", videoUrl)
                Log.d("zzz", "srartDateAfterCon:  " + startDate)
                startActivity(intent)
            } else if (currentDate.after(new_start_Date) && currentDate.before(
                    new_end_Date)
                )
             {
                Log.d("zzz", "Check condition called:  333")
                // Play video
                val intent = Intent(activity, LiveEventActivity::class.java)
                intent.putExtra("videoUrl", videoUrl)
                intent.putExtra("thumb", thumb)
                startActivity(intent)

            }
        }
    }


}