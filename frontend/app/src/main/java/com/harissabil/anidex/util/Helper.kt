package com.harissabil.anidex.util

import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun localizeDate(dateInput: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault())
    val dateString: String
    try {
        val date = sdf.parse(dateInput)
        val dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        dateString = dateFormat.format(date!!)
    } catch (e: ParseException) {
        Log.e("DATE", "Error parsing date", e)
        e.printStackTrace()
        return dateInput
    }

    return dateString
}