package com.tvuniverse.tv.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.tvuniverse.tv.R

class LiveEventActivity : Activity() {
    private var simpleExoPlayer_: ExoPlayer? = null
    private lateinit var playerView_: PlayerView
    var videoUrl:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_event)
        playerView_ = findViewById(R.id.playerView)
        videoUrl= intent.getStringExtra("videoUrl").toString()
    }

    override fun onResume() {
        super.onResume()
        playVideo(videoUrl)
    }
    fun playVideo(url: String) {
        if (simpleExoPlayer_ != null) {
            simpleExoPlayer_!!.release()
        }
        simpleExoPlayer_ = ExoPlayer.Builder(this).build()
        playerView_.player = simpleExoPlayer_
        playerView_.useController = false
        playerView_.subtitleView!!.setBottomPaddingFraction(0.17f)

        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        if (url.contains(".m3u8")) {
            Log.d("zzz","VideoUrl"+ url)
            val mediaItem = MediaItem.fromUri(url)
            val mediaSource =
                HlsMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
            simpleExoPlayer_?.setMediaSource(mediaSource)
        }
        else{
            Log.d("zzz","VideoUrl++++ mp4"+ url)
            val mediaItem = MediaItem.fromUri(url)
            playerView_.player = simpleExoPlayer_
            simpleExoPlayer_!!.setMediaItem(mediaItem)
        }
        simpleExoPlayer_?.seekTo(0)
        simpleExoPlayer_?.prepare()
        simpleExoPlayer_?.playWhenReady = true
        simpleExoPlayer_!!.addListener(object : Player.Listener {
            fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
            }
            override fun onLoadingChanged(isLoading: Boolean) {
            }
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
//                        progress_details.visibility = View.GONE

                    }
                    Player.STATE_BUFFERING -> {


                    }
                    Player.STATE_ENDED -> {
                        if (simpleExoPlayer_ != null) {
                            simpleExoPlayer_!!.release()
                            finish()

                        }
                    }
                }
            }
            override fun onRepeatModeChanged(repeatMode: Int) {
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            }
            override fun onPlayerError(error: PlaybackException) {
                if (simpleExoPlayer_ != null) {
                    simpleExoPlayer_!!.release()
                }

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

    override fun onBackPressed() {
        super.onBackPressed()
        if (simpleExoPlayer_ != null) {
            simpleExoPlayer_!!.release()
        }


    }
    override fun onPause() {
        super.onPause()
        if (simpleExoPlayer_ != null) {
            simpleExoPlayer_!!.release()
        }
    }

    private fun startContinualFastForward() {
        playerView_!!.showController()
        playerView_!!.useController = true
        playerView_!!.controllerShowTimeoutMs = 5000
//        playerView!!.FastForwardIncrementMs(15000)
    }

    private fun startContinualRewind() {
        playerView_!!.showController()
        playerView_!!.useController = true
        playerView_!!.controllerShowTimeoutMs = 5000
//        playerView!!.setRewindIncrementMs(15000)
    }
}