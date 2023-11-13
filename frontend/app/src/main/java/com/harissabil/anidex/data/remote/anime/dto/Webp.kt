package com.harissabil.anidex.data.remote.anime.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Webp(
    val image_url: String,
    val large_image_url: String,
    val small_image_url: String
) : Parcelable