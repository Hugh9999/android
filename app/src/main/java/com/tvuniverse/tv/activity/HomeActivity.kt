package com.tvuniverse.tv.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.volley.VolleyError
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.tvuniverse.tv.R
import com.tvuniverse.tv.fragment.HomeFragment
import org.json.JSONException
import org.json.JSONObject

class HomeActivity : FragmentActivity() {
    private var simpleExoPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    var homeFragment: HomeFragment? = null
    private val handler = Handler()

    private val ERR_UNKNOWN_STATUS_CODE = ""
    private val ERR_GENERIC = ""

    companion object{
        lateinit var progress_home: ProgressBar
        lateinit var playerThumb: ImageView
        lateinit var textTitle: TextView
        lateinit var textDescription: TextView

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        playerThumb =findViewById(R.id.playerThumb)
        playerView = findViewById(R.id.playerView)
        progress_home =findViewById(R.id.progress_home)
        textTitle =findViewById(R.id.textTitle)
        textDescription =findViewById(R.id.textDescription)
        homeFragment = supportFragmentManager.findFragmentById(R.id.homeFragment) as HomeFragment
        homeFragment!!.loadData()

    }

    override fun onResume() {
        super.onResume()
        restartExoPlayer()
    }

    fun playVideo(url: String) {
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.release()
        }
        playerThumb.visibility=View.VISIBLE
        simpleExoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = simpleExoPlayer
        playerView.useController = false
        playerView.subtitleView!!.setBottomPaddingFraction(0.17f)

        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        if (url.contains(".m3u8")) {
            Log.d("zzz","VideoUrl"+ url)
            val mediaItem = MediaItem.fromUri(url)
            val mediaSource =
                HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
            simpleExoPlayer?.setMediaSource(mediaSource)
        }
        else{
            Log.d("zzz","VideoUrl++++ mp4"+ url)
            val mediaItem = MediaItem.fromUri(url)
            playerView.player = simpleExoPlayer
            simpleExoPlayer!!.setMediaItem(mediaItem)
        }
        simpleExoPlayer?.seekTo(0)
        simpleExoPlayer?.prepare()
        simpleExoPlayer?.playWhenReady = true
        simpleExoPlayer!!.addListener(object : Player.Listener {
            fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
            }
            override fun onLoadingChanged(isLoading: Boolean) {
            }
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        playerThumb.visibility=View.INVISIBLE

                    }
                    Player.STATE_BUFFERING -> {
                            playerThumb.visibility=View.VISIBLE

                                            }
                    Player.STATE_ENDED -> {

                    }
                }
            }
            override fun onRepeatModeChanged(repeatMode: Int) {
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            }
            override fun onPlayerError(error: PlaybackException) {
                playerThumb.visibility=View.VISIBLE
            }

            override fun onPositionDiscontinuity(reason: Int) {
//                duration=((simpleExoPlayer!!.duration/1000).toDouble())
//                position=((simpleExoPlayer!!.contentPosition/1000).toDouble())
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            }

            override fun onSeekProcessed() {
//                duration=((simpleExoPlayer!!.duration/1000).toDouble())
//                position=((simpleExoPlayer!!.contentPosition/1000).toDouble())
            }
        })
    }

    fun stopPlayer()
    {
        simpleExoPlayer?.playWhenReady = false

    }

    private fun restartExoPlayer()  {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (simpleExoPlayer != null) {
                    simpleExoPlayer!!.release()
                    playerThumb.visibility=View.VISIBLE
                }
                handler.postDelayed(this, 20 * 1 * 1000)
            }
        }, 20 * 1 * 1000)
    }

    override fun onPause() {
        super.onPause()
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.release()
        }
        handler.removeCallbacksAndMessages(null)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.release()
            simpleExoPlayer!!.pause()
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
                        if (`object`.has("message")) {
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