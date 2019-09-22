package br.pprojects.questioncollectionapp.util

import android.content.Context
import android.view.View
import android.app.AlertDialog


fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun createDialog(context: Context, title: String, message: String) {
    val builder = AlertDialog.Builder(context)

    builder
        .setTitle(title)
        .setMessage(message)
        .create().show()
}


