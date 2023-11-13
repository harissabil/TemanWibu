package com.harissabil.anidex.data.repository

import com.harissabil.anidex.data.remote.projekbasdat.ProjekBasdatApi
import com.harissabil.anidex.data.remote.projekbasdat.dto.auth.LoginResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.auth.RegisterResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.library.CheckLibraryResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.library.CreateLibraryResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.library.DeleteLibraryResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.library.ReadLibraryResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.library.UpdateLibraryResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.review.DeleteReviewResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.review.ReadAllReviewResponse
import com.harissabil.anidex.data.remote.projekbasdat.dto.review.ReadReviewResponse
import com.harissabil.anidex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProjekBasdatRepository @Inject constructor(
    private val api: ProjekBasdatApi
) {

    fun register(
        username: String,
        password: String,
        name: String,
        email: String
    ): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.register(username, password, name, email)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
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

    fun login(
        username: String,
        password: String
    ): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.login(username, password)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
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

    fun checkLibrary(
        username: String,
        animeId: Int
    ): Flow<Resource<CheckLibraryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.checkLibrary(username, animeId)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun createLibrary(
        username: String,
        animeId: Int,
        currentStatus: String,
        title: String,
        posterImage: String,
        rating: Double,
        episode: Int,
        animeReview: String,
        animeScore: Double,
    ): Flow<Resource<CreateLibraryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.createLibrary(
                username,
                animeId,
                currentStatus,
                title,
                posterImage,
                rating,
                episode,
                animeReview,
                animeScore
            )
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun createLibraryWithoutReview(
        username: String,
        animeId: Int,
        currentStatus: String,
        title: String,
        posterImage: String,
        rating: Double,
        episode: Int,
    ): Flow<Resource<CreateLibraryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.createLibraryWithoutReview(
                username,
                animeId,
                currentStatus,
                title,
                posterImage,
                rating,
                episode,
            )
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun updateLibrary(
        username: String,
        animeId: Int,
        currentStatus: String,
        title: String,
        posterImage: String,
        rating: Double,
        episode: Int,
        animeReview: String,
        animeScore: Double,
    ): Flow<Resource<UpdateLibraryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.updateLibrary(
                username,
                animeId,
                currentStatus,
                title,
                posterImage,
                rating,
                episode,
                animeReview,
                animeScore
            )
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun updateLibraryWithoutReview(
        username: String,
        animeId: Int,
        currentStatus: String,
        title: String,
        posterImage: String,
        rating: Double,
        episode: Int,
    ): Flow<Resource<UpdateLibraryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.updateLibraryWithoutReview(
                username,
                animeId,
                currentStatus,
                title,
                posterImage,
                rating,
                episode,
            )
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun readLibrary(username: String): Flow<Resource<ReadLibraryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.readLibrary(username)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun deleteLibrary(
        username: String,
        animeId: Int
    ): Flow<Resource<DeleteLibraryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.deleteLibrary(username, animeId)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun readReview(username: String): Flow<Resource<ReadReviewResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.readReview(username)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun readAllReview(): Flow<Resource<ReadAllReviewResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.readAllReview()
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }

    fun deleteReview(
        reviewId: Int
    ): Flow<Resource<DeleteReviewResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.deleteReview(reviewId)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.message(),
                    data = null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage,
                    data = null
                )
            )
        }
    }
}