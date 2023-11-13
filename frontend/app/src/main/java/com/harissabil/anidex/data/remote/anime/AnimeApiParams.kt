package com.harissabil.anidex.data.remote.anime

import kotlin.time.Duration.Companion.seconds

object AnimeApiParams {
    const val secureBaseUrl = "https://api.jikan.moe/v4/"

    //10 MB cache
//    const val cacheSize = (10 * 1024 * 1024).toLong()

    object Timeouts {
        val connect = 10.seconds
        val write = 10.seconds
        val read = 10.seconds
    }
}

//"https://api.jikan.moe/v4/"
//"https://api.themoviedb.org/"