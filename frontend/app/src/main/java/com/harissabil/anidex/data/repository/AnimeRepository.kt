package com.harissabil.anidex.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.harissabil.anidex.data.remote.anime.AnimeApi
import com.harissabil.anidex.data.remote.anime.dto.AnimeByIdResponse
import com.harissabil.anidex.data.remote.anime.paging.NETWORK_PAGE_SIZE
import com.harissabil.anidex.data.remote.anime.paging.PopularPagingSource
import com.harissabil.anidex.data.remote.anime.paging.SearchPagingSource
import com.harissabil.anidex.data.remote.anime.paging.SeasonNowPagingSource
import com.harissabil.anidex.data.remote.anime.paging.SeasonUpcomingPagingSource
import com.harissabil.anidex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class AnimeRepository(
    private val api: AnimeApi
) {

    fun getPopularAnime() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
        ),
        pagingSourceFactory = {
            PopularPagingSource(
                apiService = api
            )
        }
    ).flow

    fun getSeasonNowAnime() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
        ),
        pagingSourceFactory = {
            SeasonNowPagingSource(
                apiService = api
            )
        }
    ).flow

    fun getSeasonUpcomingAnime() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
        ),
        pagingSourceFactory = {
            SeasonUpcomingPagingSource(
                apiService = api
            )
        }
    ).flow

    fun getAnimeById(id: Int): Flow<Resource<AnimeByIdResponse>> = flow {
        emit(Resource.Loading())
        try {
            val anime = api.getAnimeById(id)
            emit(Resource.Success(anime))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong.",
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = null
                )
            )
        }
    }

    fun getAnimeSearch(query: String) = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            SearchPagingSource(
                apiService = api,
                query = query
            )
        }
    ).flow
}