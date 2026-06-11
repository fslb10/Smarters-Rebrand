package com.example.iptvplayer.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data models for the Xtream Codes player API. Field names follow the
 * JSON returned by `player_api.php`.
 */

data class XtreamAuthResponse(
    @SerializedName("user_info") val userInfo: UserInfo? = null,
    @SerializedName("server_info") val serverInfo: ServerInfo? = null
)

data class UserInfo(
    /** 1 when the credentials are valid. */
    @SerializedName("auth") val auth: Int = 0,
    /** Typically "Active", "Expired", "Disabled", "Banned". */
    @SerializedName("status") val status: String? = null,
    @SerializedName("exp_date") val expDate: String? = null,
    @SerializedName("max_connections") val maxConnections: String? = null,
    @SerializedName("active_cons") val activeConnections: String? = null
)

data class ServerInfo(
    @SerializedName("url") val url: String? = null,
    @SerializedName("port") val port: String? = null,
    @SerializedName("https_port") val httpsPort: String? = null,
    @SerializedName("server_protocol") val serverProtocol: String? = null
)

data class LiveCategory(
    @SerializedName("category_id") val categoryId: String = "",
    @SerializedName("category_name") val categoryName: String = ""
)

data class LiveStream(
    @SerializedName("num") val num: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("stream_id") val streamId: Int = 0,
    @SerializedName("stream_icon") val streamIcon: String? = null,
    @SerializedName("epg_channel_id") val epgChannelId: String? = null,
    @SerializedName("category_id") val categoryId: String? = null
)
