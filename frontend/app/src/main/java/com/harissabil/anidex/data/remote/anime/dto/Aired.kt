package com.harissabil.anidex.data.remote.anime.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Aired(
    val from: String,
    val prop: Prop,
    val string: String?,
    val to: String
) : Parcelable