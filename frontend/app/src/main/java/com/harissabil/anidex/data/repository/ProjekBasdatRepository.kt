package com.harissabil.anidex.data.repository

import com.google.gson.Gson
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: RegisterResponse? =
                Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: LoginResponse? =
                Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: CheckLibraryResponse? =
                Gson().fromJson(errorBody, CheckLibraryResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: CreateLibraryResponse? =
                Gson().fromJson(errorBody, CreateLibraryResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: CreateLibraryResponse? =
                Gson().fromJson(errorBody, CreateLibraryResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: UpdateLibraryResponse? =
                Gson().fromJson(errorBody, UpdateLibraryResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: UpdateLibraryResponse? =
                Gson().fromJson(errorBody, UpdateLibraryResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
        }
    }

    fun readLibrary(username: String): Flow<Resource<ReadLibraryResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.readLibrary(username)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: ReadLibraryResponse? =
                Gson().fromJson(errorBody, ReadLibraryResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: DeleteLibraryResponse? =
                Gson().fromJson(errorBody, DeleteLibraryResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
        }
    }

    fun readReview(username: String): Flow<Resource<ReadReviewResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.readReview(username)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: ReadReviewResponse? =
                Gson().fromJson(errorBody, ReadReviewResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
        }
    }

    fun readAllReview(): Flow<Resource<ReadAllReviewResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.readAllReview()
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: ReadAllReviewResponse? =
                Gson().fromJson(errorBody, ReadAllReviewResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
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
            val errorMessage = e.response()?.message() ?: "Oops, something went wrong."
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse: DeleteReviewResponse? =
                Gson().fromJson(errorBody, DeleteReviewResponse::class.java)
            emit(Resource.Error(message = errorMessage, data = errorResponse))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Failed to connect to server", data = null))
        }
    }
}