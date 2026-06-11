package com.example.iptvplayer.data.remote

import com.example.iptvplayer.data.model.LiveCategory
import com.example.iptvplayer.data.model.LiveStream
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

    @GET("player_api.php")
    suspend fun getLiveCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_live_categories"
    ): List<LiveCategory>

    /** When [categoryId] is null the server returns every live stream. */
    @GET("player_api.php")
    suspend fun getLiveStreams(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String = "get_live_streams",
        @Query("category_id") categoryId: String? = null
    ): List<LiveStream>
}
