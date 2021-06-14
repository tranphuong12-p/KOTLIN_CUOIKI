package com.example.mediaplayer

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.adapter.adaptersong
import com.example.mediaplayer.adapter.spacealbum
import com.example.mediaplayer.dbhelper.musicdbhelper
import com.example.mediaplayer.model.music_model
import kotlinx.android.synthetic.main.search.*

class SearchActivity : AppCompatActivity() {
    lateinit var music: musicdbhelper
    var listSearch = mutableListOf<music_model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)
        setSupportActionBar(nav_bar_default as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var intent = intent
        val bundle = intent.extras
        if (bundle !== null){
            var nodung = bundle.getString("noidung")
            music= musicdbhelper(this)
            var listMusic = music.readAllMusic()
            listMusic.forEach {
                if(it.name.contains(nodung!!, ignoreCase = true)){
                    listSearch.add(it)
                }
            }
        }
        song(listSearch,rv_search,this)
    }
    fun song(mlistSong: MutableList<music_model>, rv_song: RecyclerView, context: Context) {
        var layoutManager: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_song.layoutManager = layoutManager
        val adapter = adaptersong(mlistSong, context)
        rv_song.adapter = adapter
        rv_song.addItemDecoration(spacealbum(1, 2))
    }

}