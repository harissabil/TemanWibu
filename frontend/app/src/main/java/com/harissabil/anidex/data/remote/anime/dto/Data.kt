package com.harissabil.anidex.data.remote.anime.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val aired: Aired,
    val airing: Boolean,
    val approved: Boolean,
    val background: String,
    val duration: String,
    val episodes: Int?,
    val favorites: Int,
    val genres: List<Genre>,
    val images: Images,
    val mal_id: Int,
    val members: Int,
    val popularity: Int,
    val rank: Int,
    val rating: String?,
    val score: Double?,
    val scored_by: Int,
    val season: String?,
    val source: String,
    val status: String,
    val studios: List<Studio>,
    val synopsis: String,
    val title: String,
    val title_english: String,
    val title_japanese: String,
    val type: String,
    val url: String,
    val year: Int?
) : Parcelable