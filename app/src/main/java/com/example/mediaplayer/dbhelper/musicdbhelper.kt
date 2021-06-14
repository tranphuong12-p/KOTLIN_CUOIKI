package com.example.mediaplayer.dbhelper


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.mediaplayer.Service
import com.example.mediaplayer.SongInfo
import com.example.mediaplayer.model.music_model
import java.lang.Exception

class musicdbhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "MediaPlayer"
        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + MusicContract.MusicEntry.TABLE_NAME + " (" +
                    MusicContract.MusicEntry.COLUMN_ID_MUSIC + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MusicContract.MusicEntry.COLUMN_URL_MUSIC + " TEXT," +
                    MusicContract.MusicEntry.COLUMN_NAME_MUSIC + " TEXT," +
                    MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC + " TEXT," +
                    MusicContract.MusicEntry.COLUMN_DURATION_MUSIC + " INTEGER," +
                    MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC + " BOOLEAN)"
        private val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MusicContract.MusicEntry.TABLE_NAME
    }

    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL(SQL_DELETE_ENTRIES)
        onCreate(p0)
    }

    fun insertMusic(music: music_model) {
        // Gets the data repository in write mode
        val db = this.writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(MusicContract.MusicEntry.COLUMN_URL_MUSIC, music.url)
        values.put(MusicContract.MusicEntry.COLUMN_NAME_MUSIC, music.name)
        values.put(MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC, music.author)
        values.put(MusicContract.MusicEntry.COLUMN_DURATION_MUSIC, music.duration)
        values.put(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC, music.isLike)

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(MusicContract.MusicEntry.TABLE_NAME, null, values)
    }

    fun readAllMusic(): MutableList<music_model> {
        val music_model = mutableListOf<music_model>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + MusicContract.MusicEntry.TABLE_NAME, null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return mutableListOf()
        }

        var music_id: Int
        var music_url: String
        var music_name: String
        var music_author: String
        var music_duration: Int
        var music_islike: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                music_id =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ID_MUSIC))
                music_url =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_URL_MUSIC))
                music_name =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_NAME_MUSIC))
                music_author =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC))
                music_duration =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_DURATION_MUSIC))
                music_islike =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC))
                music_model.add(
                    music_model(
                        music_id,
                        music_url,
                        music_name,
                        music_author,
                        music_duration,
                        music_islike > 0
                    )
                )
                cursor.moveToNext()
            }
        }
        return music_model
    }

    fun readAllMusicidAlbum(idAlbum: Int): MutableList<music_model> {
        val music_model = mutableListOf<music_model>()
        val db = writableDatabase
        db.execSQL(relationdbhelper.SQL_CREATE_ENTRIES)
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(
                "SELECT " + MusicContract.MusicEntry.COLUMN_ID_MUSIC + ","
                        + MusicContract.MusicEntry.COLUMN_URL_MUSIC + ","
                        + MusicContract.MusicEntry.COLUMN_NAME_MUSIC + ","
                        + MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC + ","
                        + MusicContract.MusicEntry.COLUMN_DURATION_MUSIC + ","
                        + MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC + " from ("+MusicContract.MusicEntry.TABLE_NAME
                        +" INNER JOIN "+RelationContract.RelationEntry.TABLE_NAME
                        + " ON "+MusicContract.MusicEntry.TABLE_NAME+"."+MusicContract.MusicEntry.COLUMN_ID_MUSIC+"="
                        +RelationContract.RelationEntry.TABLE_NAME+"."+RelationContract.RelationEntry.COLUMN_ID_Music+") where idAlbum="+idAlbum
                , null
            )
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return mutableListOf()
        }

        var music_id: Int
        var music_url: String
        var music_name: String
        var music_author: String
        var music_duration: Int
        var music_islike: Int
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                music_id =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ID_MUSIC))
                music_url =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_URL_MUSIC))
                music_name =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_NAME_MUSIC))
                music_author =
                    cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC))
                music_duration =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_DURATION_MUSIC))
                music_islike =
                    cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC))
                music_model.add(
                    music_model(
                        music_id,
                        music_url,
                        music_name,
                        music_author,
                        music_duration,
                        music_islike > 0
                    )
                )
                cursor.moveToNext()
            }
        }
        return music_model
    }
    fun addSong(song: SongInfo) {
        val query = String.format(
            "SELECT * FROM %s WHERE %s = \"%s\"",
            MusicContract.MusicEntry.TABLE_NAME,
            MusicContract.MusicEntry.COLUMN_URL_MUSIC,
            song.mSongURL
        )
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        if (cursor !== null) {
            cursor.moveToFirst()
            if (cursor.count == 0) {
                val values = ContentValues()
                values.put(MusicContract.MusicEntry.COLUMN_URL_MUSIC, song.mSongURL)
                values.put(MusicContract.MusicEntry.COLUMN_NAME_MUSIC, song.mTitle)
                values.put(MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC, song.mAuthorName)
                values.put(MusicContract.MusicEntry.COLUMN_DURATION_MUSIC, song.mSize)
                values.put(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC, false)
                db.insert(MusicContract.MusicEntry.TABLE_NAME, null, values)
            }
        }
        db.close()
    }
    fun getSongByUrl(url: String): music_model? {
        val query = String.format(
            "SELECT * FROM %s WHERE %s = \"%s\"",
            MusicContract.MusicEntry.TABLE_NAME,
            MusicContract.MusicEntry.COLUMN_URL_MUSIC,
            url
        )

        val dbread = this.readableDatabase
        val cursor: Cursor = dbread.rawQuery(query, null)
        var music: music_model? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ID_MUSIC))
            val url = cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_URL_MUSIC))
            val name = cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_NAME_MUSIC))
            val author = cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC))
            val duration = cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_DURATION_MUSIC))
            val isLike = cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC)) > 0
            music = music_model(id, url, name, author, duration, isLike)
        }

        dbread.close()
        return music
    }

    fun likeOrUnlikeSongByUrl(url: String) {
        val query = String.format(
            "SELECT * FROM %s WHERE %s = \"%s\"",
            MusicContract.MusicEntry.TABLE_NAME,
            MusicContract.MusicEntry.COLUMN_URL_MUSIC,
            url
        )
        val db = this.writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        if (cursor !== null) {
            if (cursor.moveToFirst()) {
                val values = ContentValues()
                val id = cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ID_MUSIC))
                val isLike = cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC)) > 0
                values.put(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC, !isLike)
                val where = "${MusicContract.MusicEntry.COLUMN_ID_MUSIC}=?"
                val whereArgs =
                    arrayOf(java.lang.String.valueOf(id))
                db.update(MusicContract.MusicEntry.TABLE_NAME, values, where, whereArgs)
            }
        } else {
            val values = ContentValues()
            val song = Service.listSongs[Service.currentPosition]
            values.put(MusicContract.MusicEntry.COLUMN_URL_MUSIC, song.mSongURL)
            values.put(MusicContract.MusicEntry.COLUMN_NAME_MUSIC, song.mTitle)
            values.put(MusicContract.MusicEntry.COLUMN_AUTHOR_MUSIC, song.mAuthorName)
            values.put(MusicContract.MusicEntry.COLUMN_DURATION_MUSIC, song.mSize)
            values.put(MusicContract.MusicEntry.COLUMN_ISLIKE_MUSIC, true)
            db.insert(MusicContract.MusicEntry.TABLE_NAME, null, values)
        }
        db.close()
    }
}