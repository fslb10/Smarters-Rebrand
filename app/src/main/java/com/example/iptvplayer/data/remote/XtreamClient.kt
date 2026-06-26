package com.example.iptvplayer.data.remote

import com.example.iptvplayer.BrandConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object XtreamClient {

    /** Many IPTV panels reject the default OkHttp UA; present a normal player UA. */
    private const val USER_AGENT =
        "Mozilla/5.0 (Linux; Android 9; AFTKMST12 Build/PS7245) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/70.0.3538.110 Mobile Safari/537.36"

    fun create(): XtreamApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val userAgent = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", USER_AGENT)
                .build()
            chain.proceed(request)
        }
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(userAgent)
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BrandConfig.retrofitBase())
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XtreamApi::class.java)
    }
}
