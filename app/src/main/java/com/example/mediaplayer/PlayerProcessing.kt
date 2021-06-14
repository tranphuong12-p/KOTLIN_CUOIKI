package com.example.mediaplayer

import android.os.Bundle
import android.view.MenuItem
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.dbhelper.musicdbhelper
import com.example.mediaplayer.model.music_model
import kotlinx.android.synthetic.main.activity_player_processing.*


class PlayerProcessing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var isPause: Boolean = false
        var isStop: Boolean = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_processing)
        setSupportActionBar(nav_bar_default as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val intent = intent
        val bundle = intent.extras
        var music: music_model? = null
        if (bundle !== null){
            tvSongName.text = bundle.getString("mTitle")
            tvAuthor.text = bundle.getString("mAuthorName")
            val size = bundle.getString("mSize")
            sbProgressPlayer.max = size!!.toInt()
            textviewNumber2.text = convertMilliseconds(size.toLong())
            buttonPause.setImageResource(R.drawable.ic_pause_circle)
            val db = musicdbhelper(this)
            music = db.getSongByUrl(Service.listSongs[Service.currentPosition].mSongURL!!)
            if (music?.isLike == true) {
                Favorite.setImageResource(R.drawable.ic_favorite_24dp)
            } else {
                Favorite.setImageResource(R.drawable.ic_favorite_border)
            }
            db.close()
        }
        buttonStop.setOnClickListener {
            if (!isStop) {
                Service.stopPlay()
                isStop = true
                isPause = true
                buttonPause.setImageResource(R.drawable.ic_playcircle)
                sbProgressPlayer.progress = 0
                textviewNumber1.text = convertMilliseconds(0)
            }
        }
        buttonPause.setOnClickListener {
            if (!isStop) {
                if (!isPause) {
                    Service.pausePlay()
                    isPause = true
                    buttonPause.setImageResource(R.drawable.ic_playcircle)
                } else {
                    Service.resumePlay()
                    isPause = false
                    buttonPause.setImageResource(R.drawable.ic_pause_circle)
                }
            } else {
                Service.startPlay(Service.listSongs[Service.currentPosition].mTitle!!)
                isStop = false
                isPause = false
                buttonPause.setImageResource(R.drawable.ic_pause_circle)
            }
        }
        buttonNext.setOnClickListener {
            Service.nextSong()
            isStop = false
            isPause = false
            buttonPause.setImageResource(R.drawable.ic_pause_circle)
            tvSongName.text = Service.listSongs[Service.currentPosition].mTitle
            tvAuthor.text = Service.listSongs[Service.currentPosition].mAuthorName
            sbProgressPlayer.max = Service.listSongs[Service.currentPosition].mSize
            val db = musicdbhelper(this)
            music = db.getSongByUrl(Service.listSongs[Service.currentPosition].mSongURL!!)
            if (music?.isLike == true) {
                Favorite.setImageResource(R.drawable.ic_favorite_24dp)
            } else {
                Favorite.setImageResource(R.drawable.ic_favorite_border)
            }
            db.close()
        }
        buttonPre.setOnClickListener {
            Service.preSong()
            isStop = false
            isPause = false
            buttonPause.setImageResource(R.drawable.ic_pause_circle)
            tvSongName.text = Service.listSongs[Service.currentPosition].mTitle
            tvAuthor.text = Service.listSongs[Service.currentPosition].mAuthorName
            sbProgressPlayer.max = Service.listSongs[Service.currentPosition].mSize
            val db = musicdbhelper(this)
            music = db.getSongByUrl(Service.listSongs[Service.currentPosition].mSongURL!!)
            if (music?.isLike == true) {
                Favorite.setImageResource(R.drawable.ic_favorite_24dp)
            } else {
                Favorite.setImageResource(R.drawable.ic_favorite_border)
            }
            db.close()
        }
        sbProgressPlayer?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                Service.seekToProgress(seek.progress)
            }
        })
        Favorite.setOnClickListener {
            val db = musicdbhelper(this)
            db.likeOrUnlikeSongByUrl(Service.listSongs[Service.currentPosition].mSongURL!!)
            music = db.getSongByUrl(Service.listSongs[Service.currentPosition].mSongURL!!)
            if (music?.isLike == true) {
                Favorite.setImageResource(R.drawable.ic_favorite_24dp)
            } else {
                Favorite.setImageResource(R.drawable.ic_favorite_border)
            }
            db.close()
        }
        var myTracking = MySongTrack()
        myTracking.start()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun convertMilliseconds(milliseconds: Long): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return "${minutes}:${seconds}"
    }
    inner class MySongTrack(): Thread() {

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
                runOnUiThread {
                    if (!Service.isStop) {
                        Service.setSbProgress(Service.mp.currentPosition)
                    }
                    val progress = Service.progress
                    sbProgressPlayer.progress = progress
                    textviewNumber1.text = convertMilliseconds(progress.toLong())
                }
            }
        }
    }
}