package com.example.mediaplayer.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.R
import com.example.mediaplayer.Detail_album
import com.example.mediaplayer.model.album_model

class adapteralbum(var mlistAlbum: MutableList<album_model>,var context: Context) : RecyclerView.Adapter<adapteralbum.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapteralbum.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.itemlist_album, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mlistAlbum.size
    }

    override fun onBindViewHolder(holder: adapteralbum.ViewHolder, position: Int) {
        val malbum: album_model = mlistAlbum[position]
        holder.namealbum.text = malbum.name
        holder.btn_chitiet.setOnClickListener {
            var intent: Intent = Intent(context, Detail_album::class.java)
            intent.putExtra("idAlbum", malbum.id)
            intent.putExtra("isLike", malbum.isLike)
            intent.putExtra("tenalbum",mlistAlbum[position].name)
            ContextCompat.startActivity(context, intent, intent.extras)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var namealbum = itemView.findViewById(R.id.title_album) as TextView
        var btn_chitiet=itemView.findViewById(R.id.relative_layout)as RelativeLayout
    }
}