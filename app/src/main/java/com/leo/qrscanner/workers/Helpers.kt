package com.leo.qrscanner.workers

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object Helpers {
    fun makeSnack(view: View, timeMs: Int, text: String) {
        Snackbar.make(view, text, timeMs).show()
    }

    fun makeToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}