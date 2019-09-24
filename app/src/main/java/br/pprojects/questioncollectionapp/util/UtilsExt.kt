package br.pprojects.questioncollectionapp.util

import android.content.Context
import android.view.View
import android.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*


fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun createDialog(context: Context, title: String, message: String) {
    val builder = AlertDialog.Builder(context)

    builder
        .setTitle(title)
        .setMessage(message)
        .create().show()
}

fun String.formatString(originalFormat: String, finalFormat: String): String {
    val originalSdf = SimpleDateFormat(originalFormat, Locale.getDefault())
    val finalSdf = SimpleDateFormat(finalFormat, Locale.getDefault())

    originalSdf.parse(this)?.let {
        return finalSdf.format(it).toString()
    } ?: run {
        return ""
    }
}


