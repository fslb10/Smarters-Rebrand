package com.example.iptvplayer.data

import com.example.iptvplayer.BrandConfig
import com.example.iptvplayer.data.model.LiveCategory
import com.example.iptvplayer.data.model.LiveStream
import com.example.iptvplayer.data.model.XtreamAuthResponse
import com.example.iptvplayer.data.remote.XtreamApi

/**
 * Single entry point for all data access. The UI talks to this, never to
 * Retrofit directly.
 */
class XtreamRepository(
    private val api: XtreamApi,
    private val session: SessionStore
) {

    /** Validates arbitrary credentials (used by the login screen). */
    suspend fun authenticate(username: String, password: String): XtreamAuthResponse =
        api.authenticate(username, password)

    suspend fun getLiveCategories(): List<LiveCategory> =
        api.getLiveCategories(session.username, session.password)

    /** [categoryId] == null returns every live stream in one request. */
    suspend fun getLiveStreams(categoryId: String? = null): List<LiveStream> =
        api.getLiveStreams(session.username, session.password, categoryId = categoryId)

    /** Builds the playable URL for a live channel. */
    fun buildLiveStreamUrl(streamId: Int): String {
        val base = BrandConfig.PORTAL_BASE_URL.trimEnd('/')
        return "$base/live/${session.username}/${session.password}/$streamId.ts"
    }
}
