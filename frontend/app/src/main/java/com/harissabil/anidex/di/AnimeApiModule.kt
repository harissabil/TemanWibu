package com.harissabil.anidex.di

import android.content.Context
import com.harissabil.anidex.BuildConfig
import com.harissabil.anidex.data.remote.anime.AnimeApi
import com.harissabil.anidex.data.remote.anime.AnimeApiParams
import com.harissabil.anidex.di.utils.ConnectivityChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.toJavaDuration

@Module
@InstallIn(SingletonComponent::class)
class AnimeApiModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, CACHE_SIZE)

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): AnimeApi {
        return retrofit.create(AnimeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            } else {
                level = HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    @Named(ONLINE_INTERCEPTOR)
    fun provideOnlineInterceptor(): Interceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 60 * 60
        response
            .newBuilder()
            .header(CACHE_CONTROL, "public, max-age=$maxAge")
            .removeHeader(PRAGMA)
            .build()
    }

    @Provides
    @Singleton
    @Named(OFFLINE_INTERCEPTOR)
    fun provideOfflineInterceptor(@ApplicationContext mContext: Context): Interceptor =
        Interceptor { chain ->
            var request = chain.request()
            val connectivityChecker = ConnectivityChecker()
            val isConnectivityAvailable = connectivityChecker(mContext)
            if (!isConnectivityAvailable) {
                val maxStale = 60 * 60 * 24 * 7
                request = request
                    .newBuilder()
                    .header(CACHE_CONTROL, "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader(PRAGMA)
                    .build()
            }
            chain.proceed(request)
        }

//    @Provides
//    @Singleton
//    @Named(AUTHENTICATION_INTERCEPTOR)
//    fun provideAuthenticationInterceptor(): Interceptor {
//        return Interceptor { chain ->
//            val request = chain.request().newBuilder()
//                .addHeader("accept", "application/json")
//                .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
//                .build()
//            chain.proceed(request)
//        }
//    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
//        @Named(AUTHENTICATION_INTERCEPTOR) authenticationInterceptor: Interceptor,
        @Named(ONLINE_INTERCEPTOR) internetInterceptor: Interceptor,
        @Named(OFFLINE_INTERCEPTOR) offlineInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(offlineInterceptor)
        .addNetworkInterceptor(internetInterceptor)
        .addInterceptor(httpLoggingInterceptor)
//        .addInterceptor(authenticationInterceptor)
        .cache(cache)
        .connectTimeout(AnimeApiParams.Timeouts.connect.toJavaDuration())
        .writeTimeout(AnimeApiParams.Timeouts.write.toJavaDuration())
        .readTimeout(AnimeApiParams.Timeouts.read.toJavaDuration())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(AnimeApiParams.secureBaseUrl.toHttpUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

private const val CACHE_SIZE: Long = (50 * 1024 * 1024).toLong()
//private const val AUTHENTICATION_INTERCEPTOR = "authentication_interceptor"
private const val ONLINE_INTERCEPTOR = "online_interceptor"
private const val OFFLINE_INTERCEPTOR = "offline_interceptor"
private const val CACHE_CONTROL = "Cache-Control"
private const val PRAGMA = "Pragma"