package com.harissabil.anidex.data.remote.anime.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Studio(
    val mal_id: Int,
    val name: String,
    val type: String,
    val url: String
) : Parcelable