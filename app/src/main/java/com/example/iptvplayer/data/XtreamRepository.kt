package com.example.iptvplayer.data

import com.example.iptvplayer.BrandConfig
import com.example.iptvplayer.data.model.Category
import com.example.iptvplayer.data.model.LiveStream
import com.example.iptvplayer.data.model.Series
import com.example.iptvplayer.data.model.SeriesInfoResponse
import com.example.iptvplayer.data.model.VodStream
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

    // ── Live TV ──────────────────────────────────────────────────────────
    suspend fun getLiveCategories(): List<Category> =
        api.getLiveCategories(session.username, session.password)

    /** [categoryId] == null returns every live stream in one request. */
    suspend fun getLiveStreams(categoryId: String? = null): List<LiveStream> =
        api.getLiveStreams(session.username, session.password, categoryId = categoryId)

    // ── Movies (VOD) ─────────────────────────────────────────────────────
    suspend fun getVodCategories(): List<Category> =
        api.getVodCategories(session.username, session.password)

    suspend fun getVodStreams(categoryId: String? = null): List<VodStream> =
        api.getVodStreams(session.username, session.password, categoryId = categoryId)

    // ── Series ───────────────────────────────────────────────────────────
    suspend fun getSeriesCategories(): List<Category> =
        api.getSeriesCategories(session.username, session.password)

    suspend fun getSeries(categoryId: String? = null): List<Series> =
        api.getSeries(session.username, session.password, categoryId = categoryId)

    suspend fun getSeriesInfo(seriesId: Int): SeriesInfoResponse =
        api.getSeriesInfo(session.username, session.password, seriesId)

    // ── Playback URL builders ────────────────────────────────────────────
    fun buildLiveStreamUrl(streamId: Int): String =
        "${base()}/live/${creds()}/$streamId.ts"

    fun buildVodStreamUrl(streamId: Int, containerExtension: String?): String =
        "${base()}/movie/${creds()}/$streamId.${containerExtension ?: "mp4"}"

    fun buildSeriesStreamUrl(episodeId: String, containerExtension: String?): String =
        "${base()}/series/${creds()}/$episodeId.${containerExtension ?: "mp4"}"

    private fun base(): String = BrandConfig.PORTAL_BASE_URL.trimEnd('/')
    private fun creds(): String = "${session.username}/${session.password}"
}
