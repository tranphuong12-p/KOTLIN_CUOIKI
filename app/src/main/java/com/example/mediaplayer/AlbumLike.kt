package com.example.mediaplayer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.adapter.adapteralbumlike
import com.example.mediaplayer.dbhelper.albumdbhelper
import com.example.mediaplayer.model.album_model
import kotlinx.android.synthetic.main.activity_album_like.*

class AlbumLike : AppCompatActivity() {
    private var mlistAlbumLike = mutableListOf<album_model>()
    lateinit var album: albumdbhelper//lop nay chua phuong thuc de tuong tac voi album
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_like)
        setSupportActionBar(nav_bar_default as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        album = albumdbhelper(this)
        mlistAlbumLike = album.readAllAlbumLike()
        album(mlistAlbumLike, rv_album_favorite, this)
    }

    fun album(mlistAlbumLike: MutableList<album_model>, rv_album_favorite: RecyclerView, context: Context) {
        var layoutManager: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_album_favorite.layoutManager = layoutManager
        //rv_album.itemAnimator=DefaultItemAnimator
        val adapter = adapteralbumlike(mlistAlbumLike,context)
        rv_album_favorite.adapter = adapter
        //rv_album_favorite.addItemDecoration(spacealbum(1,2))
    }
}