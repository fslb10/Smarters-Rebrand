package com.example.iptvplayer

/**
 * ───────────────────────────────────────────────────────────────────────────
 *  BRANDING & PORTAL CONFIGURATION
 *  This is the ONE file you edit to point the app at your server and to
 *  set your brand. Nothing here is secret — it is just app configuration.
 * ───────────────────────────────────────────────────────────────────────────
 *
 *  PORTAL_BASE_URL is your IPTV provider's server address (the "portal URL").
 *  It must include the scheme (http/https), the host, and the port, e.g.:
 *
 *      "http://your-portal-domain.example:8080/"
 *
 *  Your users will NEVER see or type this — they only enter the username and
 *  password your provider gave them. Keep the trailing slash.
 *
 *  ⚠️ Only point this at a portal you are authorized to use (your own
 *     subscription / your own reseller panel). Do not distribute the app
 *     configured against servers you do not have rights to.
 */
object BrandConfig {

    /** Replace this placeholder with your real portal URL. Keep the trailing slash. */
    const val PORTAL_BASE_URL: String = "http://your-portal-domain.example:8080/"

    /** Returns a Retrofit-safe base URL (guaranteed trailing slash). */
    fun retrofitBase(): String =
        if (PORTAL_BASE_URL.endsWith("/")) PORTAL_BASE_URL else "$PORTAL_BASE_URL/"

    /** Human-readable host shown on the login screen ("Connected to …"). */
    fun displayHost(): String =
        PORTAL_BASE_URL
            .substringAfter("://")
            .trimEnd('/')
}
