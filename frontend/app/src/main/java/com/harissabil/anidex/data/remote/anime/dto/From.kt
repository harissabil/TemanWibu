package com.harissabil.anidex.data.remote.anime.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class From(
    val day: Int,
    val month: Int,
    val year: Int
) : Parcelable