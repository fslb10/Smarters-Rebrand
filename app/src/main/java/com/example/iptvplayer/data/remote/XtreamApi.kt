package com.example.iptvplayer.data.remote

import com.example.iptvplayer.data.model.Category
import com.example.iptvplayer.data.model.LiveStream
import com.example.iptvplayer.data.model.Series
import com.example.iptvplayer.data.model.SeriesInfoResponse
import com.example.iptvplayer.data.model.VodStream
import com.example.iptvplayer.data.model.XtreamAuthResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The Xtream Codes player API. All calls hit `player_api.php` and are
 * differentiated by the `action` query parameter. The base URL is the
 * provider's portal (see [com.example.iptvplayer.BrandConfig]).
 */
interface XtreamApi {

    @GET("player_api.php")
    suspend fun authenticate(
        @Query("username") username: String,
        @Query("password") password: String
    ): XtreamAuthResponse

    // ── Live TV ──────────────────────────────────────────────────────────
    @GET("player_api.php")
    suspend fun getLiveCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_live_categories"
    ): List<Category>

    /** When [categoryId] is null the server returns every live stream. */
    @GET("player_api.php")
    suspend fun getLiveStreams(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_live_streams",
        @Query("category_id") categoryId: String? = null
    ): List<LiveStream>

    // ── Movies (VOD) ─────────────────────────────────────────────────────
    @GET("player_api.php")
    suspend fun getVodCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_vod_categories"
    ): List<Category>

    @GET("player_api.php")
    suspend fun getVodStreams(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_vod_streams",
        @Query("category_id") categoryId: String? = null
    ): List<VodStream>

    // ── Series ───────────────────────────────────────────────────────────
    @GET("player_api.php")
    suspend fun getSeriesCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_series_categories"
    ): List<Category>

    @GET("player_api.php")
    suspend fun getSeries(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_series",
        @Query("category_id") categoryId: String? = null
    ): List<Series>

    @GET("player_api.php")
    suspend fun getSeriesInfo(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("series_id") seriesId: Int,
        @Query("action") action: String = "get_series_info"
    ): SeriesInfoResponse
}
