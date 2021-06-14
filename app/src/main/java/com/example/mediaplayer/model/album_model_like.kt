package com.example.mediaplayer.model

import java.io.Serializable

class album_model_like:Serializable
{
    private var id:Int?=null
    private var name:String?=null
    private var isLike:Boolean?=null
    constructor()
    constructor(id: Int?, name: String?, isLike: Boolean?) {
        this.id = id
        this.name = name
        this.isLike = isLike
    }
}