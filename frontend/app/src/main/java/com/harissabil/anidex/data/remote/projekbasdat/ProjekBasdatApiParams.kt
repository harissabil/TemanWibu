package com.harissabil.anidex.data.remote.projekbasdat

import com.harissabil.anidex.BuildConfig
import kotlin.time.Duration.Companion.seconds

object ProjekBasdatApiParams {
    const val secureBaseUrl = BuildConfig.BACKEND_URL

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