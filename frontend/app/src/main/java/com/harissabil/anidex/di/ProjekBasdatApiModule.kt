package com.harissabil.anidex.di

import com.harissabil.anidex.BuildConfig
import com.harissabil.anidex.data.remote.projekbasdat.ProjekBasdatApi
import com.harissabil.anidex.data.remote.projekbasdat.ProjekBasdatApiParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.toJavaDuration

@Module
@InstallIn(SingletonComponent::class)
class ProjekBasdatApiModule {

    @Provides
    @Singleton
    fun provideApi(@Named(PROJEK_BASDAT_RETROFIT) retrofit: Retrofit): ProjekBasdatApi {
        return retrofit.create(ProjekBasdatApi::class.java)
    }

    @Provides
    @Singleton
    @Named(PROJEK_BASDAT_LOGGING_INTERCEPTOR)
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    @Named(PROJEK_BASDAT_CLIENT)
    fun provideOkHttpClient(
        @Named(PROJEK_BASDAT_LOGGING_INTERCEPTOR) httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(ProjekBasdatApiParams.Timeouts.connect.toJavaDuration())
        .writeTimeout(ProjekBasdatApiParams.Timeouts.write.toJavaDuration())
        .readTimeout(ProjekBasdatApiParams.Timeouts.read.toJavaDuration())
        .build()

    @Provides
    @Singleton
    @Named(PROJEK_BASDAT_RETROFIT)
    fun provideRetrofit(@Named(PROJEK_BASDAT_CLIENT) client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ProjekBasdatApiParams.secureBaseUrl.toHttpUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

private const val PROJEK_BASDAT_LOGGING_INTERCEPTOR = "projek_basdat_logging_interceptor"
private const val PROJEK_BASDAT_CLIENT = "projek_basdat_client"
private const val PROJEK_BASDAT_RETROFIT = "projek_basdat_retrofit"