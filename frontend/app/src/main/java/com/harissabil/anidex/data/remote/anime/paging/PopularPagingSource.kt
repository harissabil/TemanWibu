package com.harissabil.anidex.data.remote.anime.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.harissabil.anidex.data.remote.anime.AnimeApi
import com.harissabil.anidex.data.remote.anime.dto.Data
import kotlinx.coroutines.delay
import javax.inject.Inject

const val NETWORK_PAGE_SIZE = 5
const val INITIAL_LOAD_SIZE = 1

class PopularPagingSource @Inject constructor(private val apiService: AnimeApi) :
    PagingSource<Int, Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val position = params.key ?: INITIAL_LOAD_SIZE
        return try {
            val response = apiService.getPopularAnime(
                page = position,
            )
            delay(
                if (position == 1) {
                    0
                } else {
                    500
                }
            )
            val nextKey = if (!response.pagination.has_next_page) {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = response.data,
                prevKey = if (position == INITIAL_LOAD_SIZE) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}