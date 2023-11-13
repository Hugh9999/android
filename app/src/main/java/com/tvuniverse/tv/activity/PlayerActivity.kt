package com.tvuniverse.tv.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.tvuniverse.tv.R

class PlayerActivity : Activity() {
    private var simpleExoPlayer_: ExoPlayer? = null
    private lateinit var playerView_: PlayerView
    var videoUrl:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        playerView_ = findViewById(R.id.player_View)
        videoUrl= intent.getStringExtra("videoUrl").toString()
    }

    override fun onResume() {
        super.onResume()
        play_Video(videoUrl)
    }


    fun play_Video(url: String) {
        if (simpleExoPlayer_ != null) {
            simpleExoPlayer_!!.release()
        }
        simpleExoPlayer_ = ExoPlayer.Builder(this).build()
        playerView_.player = simpleExoPlayer_
        playerView_.useController = true
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var goUp: Boolean = false
        when (keyCode) {
            19 -> { // UP


            }

            20 -> { // DOWN
                    playerView_.showController()
                    playerView_.useController = true
                    simpleExoPlayer_!!.playWhenReady = true
                    playerView_.controllerShowTimeoutMs = 4000


            }
            21 -> {

            }

            90 -> {

            }
        }
        return super.onKeyDown(keyCode, event)
    }

}