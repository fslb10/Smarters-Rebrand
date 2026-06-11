package com.example.iptvplayer.data

import android.content.Context

/**
 * Persists the logged-in user's credentials. They are needed both to call
 * the API and to build per-stream playback URLs.
 *
 * NOTE: credentials are stored in plain SharedPreferences here for clarity.
 * For a production release consider EncryptedSharedPreferences
 * (androidx.security:security-crypto).
 */
class SessionStore(context: Context) {

    private val prefs =
        context.getSharedPreferences("iptv_session", Context.MODE_PRIVATE)

    val username: String get() = prefs.getString(KEY_USER, "").orEmpty()
    val password: String get() = prefs.getString(KEY_PASS, "").orEmpty()

    fun save(username: String, password: String) {
        prefs.edit()
            .putString(KEY_USER, username)
            .putString(KEY_PASS, password)
            .apply()
    }

    fun isLoggedIn(): Boolean = username.isNotBlank() && password.isNotBlank()

    fun logout() = prefs.edit().clear().apply()

    private companion object {
        const val KEY_USER = "username"
        const val KEY_PASS = "password"
    }
}
