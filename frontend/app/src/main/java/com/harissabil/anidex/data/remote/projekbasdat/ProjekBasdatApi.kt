package com.harissabil.anidex.data.remote.projekbasdat

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
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ProjekBasdatApi {

    @FormUrlEncoded
    @POST("auth/register.php")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("email") email: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login.php")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("library/check.php")
    suspend fun checkLibrary(
        @Field("username") username: String,
        @Field("anime_id") animeId: Int,
    ): CheckLibraryResponse

    @FormUrlEncoded
    @POST("library/create.php")
    suspend fun createLibrary(
        @Field("username") username: String,
        @Field("anime_id") animeId: Int,
        @Field("current_status") currentStatus: String,
        @Field("title") title: String,
        @Field("poster_image") posterImage: String,
        @Field("rating") rating: Double,
        @Field("episode") episode: Int,
        @Field("anime_review") animeReview: String,
        @Field("anime_score") animeScore: Double,
    ): CreateLibraryResponse

    @FormUrlEncoded
    @POST("library/create.php")
    suspend fun createLibraryWithoutReview(
        @Field("username") username: String,
        @Field("anime_id") animeId: Int,
        @Field("current_status") currentStatus: String,
        @Field("title") title: String,
        @Field("poster_image") posterImage: String,
        @Field("rating") rating: Double,
        @Field("episode") episode: Int,
    ): CreateLibraryResponse

    @FormUrlEncoded
    @POST("library/read.php")
    suspend fun readLibrary(
        @Field("username") username: String,
    ): ReadLibraryResponse

    @FormUrlEncoded
    @POST("library/update.php")
    suspend fun updateLibrary(
        @Field("username") username: String,
        @Field("anime_id") animeId: Int,
        @Field("current_status") currentStatus: String,
        @Field("title") title: String,
        @Field("poster_image") posterImage: String,
        @Field("rating") rating: Double,
        @Field("episode") episode: Int,
        @Field("anime_review") animeReview: String,
        @Field("anime_score") animeScore: Double,
    ): UpdateLibraryResponse

    @FormUrlEncoded
    @POST("library/update.php")
    suspend fun updateLibraryWithoutReview(
        @Field("username") username: String,
        @Field("anime_id") animeId: Int,
        @Field("current_status") currentStatus: String,
        @Field("title") title: String,
        @Field("poster_image") posterImage: String,
        @Field("rating") rating: Double,
        @Field("episode") episode: Int,
    ): UpdateLibraryResponse

    @FormUrlEncoded
    @POST("library/delete.php")
    suspend fun deleteLibrary(
        @Field("username") username: String,
        @Field("anime_id") animeId: Int,
    ): DeleteLibraryResponse

    @FormUrlEncoded
    @POST("review/read.php")
    suspend fun readReview(
        @Field("username") username: String,
    ): ReadReviewResponse

    @GET("review/readAll.php")
    suspend fun readAllReview(): ReadAllReviewResponse

    @FormUrlEncoded
    @POST("review/delete.php")
    suspend fun deleteReview(
        @Field("review_id") reviewId: Int,
    ): DeleteReviewResponse
}