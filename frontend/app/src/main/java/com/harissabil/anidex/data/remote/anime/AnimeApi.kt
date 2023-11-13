package com.harissabil.anidex.data.remote.anime

import com.harissabil.anidex.data.remote.anime.dto.AnimeByIdResponse
import com.harissabil.anidex.data.remote.anime.dto.AnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeApi {

    @GET("top/anime")
    suspend fun getPopularAnime(
        @Query("page") page: Int,
    ): AnimeResponse

    @GET("seasons/now")
    suspend fun getSeasonNowAnime(
        @Query("page") page: Int,
    ): AnimeResponse

    @GET("seasons/upcoming")
    suspend fun getSeasonUpcomingAnime(
        @Query("page") page: Int,
    ): AnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeById(
        @Path("id") id: Int,
    ): AnimeByIdResponse

    @GET("anime")
    suspend fun getAnimeSearch(
        @Query("q") q: String,
        @Query("page") page: Int,
        @Query("sfw") sfw: Boolean = true
    ): AnimeResponse
}