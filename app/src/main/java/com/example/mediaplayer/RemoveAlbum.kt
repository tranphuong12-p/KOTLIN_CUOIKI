package com.example.mediaplayer

import android.R.*
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.mediaplayer.dbhelper.albumdbhelper

class RemoveAlbum(
    var context: Any,
    val idAlbum: Int
) : AppCompatDialogFragment() {
    private lateinit var albumdbhelper: albumdbhelper
    private  var listener: RemoveAlbum.DialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        albumdbhelper = albumdbhelper(context as Activity)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Bạn có đồng ý xóa album này không ?")
                // Add action buttons
                .setPositiveButton("Đồng ý",
                    DialogInterface.OnClickListener { dialog, id ->
                        albumdbhelper.removeAlbumById(idAlbum);
                        listener?.goBack()
                    })
                .setNegativeButton("Hủy bỏ",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.setTitle("Xác nhận xóa album")
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement DialogListener")
        }
    }
    interface DialogListener  {
        fun goBack()
    }
}