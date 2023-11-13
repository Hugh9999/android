package com.tvuniverse.tv.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.tvuniverse.tv.R
import com.tvuniverse.tv.activity.HomeActivity
import com.tvuniverse.tv.activity.PlayerActivity
import com.tvuniverse.tv.activity.SeriesActivity
import com.tvuniverse.tv.model.HomeModel
import com.tvuniverse.tv.presenter.CardPresenter
import com.tvuniverse.tv.singleton.VolleySingleton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class SeriesFragment : BrowseSupportFragment() {
    private var rowAdapter: ArrayObjectAdapter? = null
    private var cardPresenterHeader: HeaderItem? = null
    private var cardRowAdapter: ArrayObjectAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUIElements()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupUIElements() {
        headersState = HEADERS_DISABLED
        isHeadersTransitionOnBackEnabled = true
        setUpEvents()

    }

    fun loadData(id: String?) {
        var listRowPresenter = ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false)
        var cardPresenter = CardPresenter()
        cardRowAdapter = ArrayObjectAdapter(cardPresenter)
        rowAdapter = ArrayObjectAdapter(listRowPresenter)
        val stringRequest: StringRequest = @SuppressLint("NotifyDataSetChanged")
        object : StringRequest(
            Method.GET, "https://aoswhy2hq2.execute-api.us-east-1.amazonaws.com/app/series/" + id,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("zzz", "SeriesFragment :$response")

                    val jsonArray = jsonObject.getJSONArray("data")
                    if (jsonArray.length() > 0) {
                        for (i in 0 until jsonArray.length()) {
                            val jsonObjectItems = jsonArray.getJSONObject(i)

//                            homeModel.status = jsonObjectItems.getString("status")
//                            homeModel.backgroundImage =
//                                jsonObjectItems.getString("background_image")

                            cardPresenterHeader = HeaderItem(jsonObjectItems.getString("title"))


                            val jsonArrayEpisode = jsonObjectItems.getJSONArray("episodes")
                            for (k in 0 until jsonArrayEpisode.length()) {
                                val homeModel = HomeModel()

                                val jsonObjectEpisodes = jsonArrayEpisode.getJSONObject(k)
                                homeModel.title = jsonObjectEpisodes.getString("title")
                                homeModel._id = jsonObjectEpisodes.getString("_id")
//                                    homeModel.type = "live"
                                homeModel.description =
                                    jsonObjectEpisodes.getString("description")
                                homeModel.thumbnail = jsonObjectEpisodes.getString("thumbnail")
                                homeModel.videoUrl=jsonObjectEpisodes.getString("video_url")
                                cardRowAdapter!!.add(homeModel)

                            }
                        }
                        rowAdapter!!.add(ListRow(cardPresenterHeader, cardRowAdapter))
                        adapter = rowAdapter
                        SeriesActivity.progress_home.visibility = View.GONE


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


    fun setUpEvents() {
        onItemViewClickedListener =
            OnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
                if (item is HomeModel) {
                    val content = item
                    val intent = Intent(activity, PlayerActivity::class.java)
                    intent.putExtra("videoUrl", content.videoUrl)
                    intent.putExtra("thumb", content.thumbnail)
                    startActivity(intent)

                }
            }
        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is HomeModel) {
                val content = item
                Glide.with(itemViewHolder.view.context)
                    .load(content.getThumbnail())
                    .into(SeriesActivity.playerThumb)

                SeriesActivity.textTitle.setText(content.title)
                SeriesActivity.textDescription.setText(content.description)
            }

        }


    }


}