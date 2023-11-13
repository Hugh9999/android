package com.tvuniverse.tv.activity

import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.tvuniverse.tv.R
import com.tvuniverse.tv.fragment.SeriesFragment

class SeriesActivity : FragmentActivity() {
    private var simpleExoPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    var homeFragment: SeriesFragment? = null

    companion object{
        lateinit var progress_home: ProgressBar
        lateinit var playerThumb: ImageView
        lateinit var textTitle: TextView
        lateinit var textDescription: TextView

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)
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
        homeFragment = supportFragmentManager.findFragmentById(R.id.seriesFragment) as SeriesFragment
        var id = intent.getStringExtra("id")
        homeFragment!!.loadData(id)
    }
}