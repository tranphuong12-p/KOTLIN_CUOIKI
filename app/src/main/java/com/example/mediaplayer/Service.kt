package com.example.mediaplayer

import android.media.MediaPlayer

class Service {
    companion object {
        var mp: MediaPlayer = MediaPlayer()
        var progress: Int = 0
        var listSongs = ArrayList<SongInfo>()
        var currentPosition = 0
        var isStop = true
        fun setSbProgress(number: Int) {
            progress = number
        }
        fun seekToProgress(number: Int) {
            mp.seekTo(number)
        }
        fun stopPlay() {
            mp.stop()
            mp.reset()
            this.isStop = true
        }
        fun pausePlay() {
            if (mp.isPlaying) {
                mp.pause()
            }
        }
        fun resumePlay() {
            if (!mp.isPlaying) {
                mp.seekTo(progress)
                mp.start()
            }
        }
        fun startPlay(songName: String) {
            var position: Int = 0
            this.listSongs.forEachIndexed{index, song ->
                run {
                    if (song.mTitle.equals(songName)) {
                        position = index
                    }
                }
            }
            if (this.currentPosition != position) {
                stopPlay()
                this.currentPosition = position
                mp.setDataSource(this.listSongs[position].mSongURL)
                mp.prepare()
                mp.start()
                this.isStop = false
            } else {
                this.currentPosition = position
                if (isStop) {
                    mp.setDataSource(this.listSongs[position].mSongURL)
                    mp.prepare()
                    mp.start()
                    this.isStop = false
                }
                resumePlay()
            }
        }
        fun setSongList (list: ArrayList<SongInfo>) {
            this.listSongs = list
        }
        fun nextSong() {
            if (!isStop) {
                stopPlay()
            }
            var nextPosition = this.currentPosition + 1
            if (nextPosition >= this.listSongs.size) {
                nextPosition = 0
            }
            mp.setDataSource(this.listSongs[nextPosition].mSongURL)
            this.currentPosition = nextPosition
            mp.prepare()
            mp.start()
            this.isStop = false
        }
        fun preSong() {
            if (!isStop) {
                stopPlay()
            }
            var prePosition = this.currentPosition - 1
            if (prePosition < 0) {
                prePosition = this.listSongs.size - 1
            }
            mp.setDataSource(this.listSongs[prePosition].mSongURL)
            this.currentPosition = prePosition
            mp.prepare()
            mp.start()
            this.isStop = false
        }
    }
}