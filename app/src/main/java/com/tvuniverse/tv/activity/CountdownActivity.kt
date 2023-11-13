package com.tvuniverse.tv.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tvuniverse.tv.R
import com.tvuniverse.tv.fragment.HomeFragment
import com.tvuniverse.tv.singleton.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CountdownActivity : Activity() {
    val timeZone = TimeZone.getDefault()
    var backGroundImage: String = ""
    lateinit var content_image: ImageView
    lateinit var contentDetailTitle: TextView
    lateinit var contentDetailGenre: TextView
    lateinit var contentCast: TextView
    lateinit var countdown: TextView
    var id:String=""
    var videoUrl:String=""
    private val ERR_UNKNOWN_STATUS_CODE = ""
    private val ERR_GENERIC = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countdown)
        content_image=findViewById(R.id.content_image)
        contentDetailGenre=findViewById(R.id.content_detail_genre)
        contentDetailTitle=findViewById(R.id.content_detail_title)
        contentCast=findViewById(R.id.content_cast)
        countdown=findViewById(R.id.countdown)
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        Log.d("zzz","CountdownActivityCalled:   "+HomeFragment.startDateAfterCon_new)
//        Log.d("zzz","CountDownActivity:   "+intent.getStringExtra("start_Date"))
//        var startDate: Date = intent.getStringExtra("start_Date",).toString()?.let { parser.parse(it) }!!
        backGroundImage = intent.getStringExtra("backGroundImage").toString()
        id = intent.getStringExtra("id").toString()
        videoUrl= intent.getStringExtra("videoUrl").toString()

//        val start_Date: Date? = parser.parse(startDate.toString())
        val currentDate = Date()
//        val s: Instant = Instant.parse(startDate.toString())
//        ZoneId.of(timeZone.id)
//        val startDateAfterCon: LocalDateTime = LocalDateTime.ofInstant(s, ZoneId.of(timeZone.id))
//        System.out.println(startDateAfterCon)

        val millisDifference = parser.parse(HomeFragment.startDateAfterCon_new.toString())!!.time-currentDate.time
            Glide.with(this)
                .load(backGroundImage)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(content_image)
            contentDetailTitle.text = intent.getStringExtra("title")

//                val commaSeparatedGenre =
//                    eventsResponseItem.genre.joinToString(separator = ", ") { it -> it }
//                contentDetailGenre.text = commaSeparatedGenre


//                val commaSeparatedCast =
//                    eventsResponseItem.cast.joinToString(separator = ", ") { it -> it }
//                contentCast.text = commaSeparatedCast



        object : CountDownTimer(millisDifference, 1000) {
            // adjust the milli seconds here
            override fun onTick(millisUntilFinished: Long) {

                val dy = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                val hr = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(
                    TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                )
                val min =
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    )
                val sec =
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )

                countdown.text =
                    java.lang.String.format(
                        "The event will go live in %dD : %dH : %dM : %dS",
                        dy,
                        hr,
                        min,
                        sec
                    )
            }

            override fun onFinish() {
                checkStreamResponse()

            }
        }.start()
    }

    fun checkStreamResponse()
    {
            val stringRequest: StringRequest = @SuppressLint("NotifyDataSetChanged")
            object : StringRequest(
                Method.GET, "https://tkg1sim6rd.execute-api.us-west-2.amazonaws.com/live-event/app/"+id,
                Response.Listener { response ->
                    try {
                        val jsonObject = JSONObject(response)

                            val intent = Intent(applicationContext, LiveEventActivity::class.java)
                            intent.putExtra("thumb", backGroundImage)
                            intent.putExtra("videoUrl", videoUrl)
                            startActivity(intent)
                            finish()


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    handleServerError(
                        it,
                        applicationContext
                    )
                    countdown.text="Sorry for the inconvenience! Event will be live shortly"

                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    return headers
                }
            }
            stringRequest.setShouldCache(false)
            applicationContext.let {
                VolleySingleton.getInstance(it).addToRequestQueue(stringRequest)
            }


    }

    private fun handleServerError(err: Any, context: Context): String? {
        val error = err as VolleyError
        val response = error.networkResponse
        return if (response != null) {
            when (response.statusCode) {
                422 -> {
                    try {
                        val string = String(error.networkResponse.data)
                        val `object` = JSONObject(string)
                        val altmsg = `object`.getString("message")
                        if (`object`.has("error_message")) {
                            countdown.text="Sorry for the inconvenience! Event will be live shortly"
                            return `object`["message"].toString()
                        } else if (`object`.has("error_description")) {
                            return `object`["error_description"].toString()
                        }
                    } catch (e: JSONException) {
                        return "Could not parse response"
                    }
                    // invalid request
                    error.message
                }
                else -> ERR_UNKNOWN_STATUS_CODE
            }
        } else ERR_GENERIC
    }
}