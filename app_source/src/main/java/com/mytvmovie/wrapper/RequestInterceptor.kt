package com.mytvmovie.wrapper

import android.net.Uri
import android.util.Log

/**
 * Very aggressive ad/explicit blocking.
 * - Blocks anything not whitelisted
 * - Blocks known ad patterns even if whitelisted host sneaks in bad paths
 */
object RequestInterceptor {
    private const val TAG = "Interceptor"

    // Basic patterns. Add more if you see new junk in logs.
    private val blockedPathHints = listOf(
        "popunder", "popup", "ads", "adserve", "doubleclick", "tracking",
        "banner", "click", "redirect", "offer", "promos", "vast", "preroll"
    )

    fun shouldBlock(url: String?): Boolean {
        if (url.isNullOrBlank()) return true

        return try {
            val uri = Uri.parse(url)
            val host = uri.host ?: return true

            // 1) Hard whitelist rule
            if (!Whitelist.isAllowed(host)) {
                Log.w(TAG, "[BLOCK host] $host  url=$url")
                return true
            }

            // 2) Path hint blocking
            val path = (uri.path ?: "").lowercase()
            if (blockedPathHints.any { path.contains(it) }) {
                Log.w(TAG, "[BLOCK path] $path  url=$url")
                return true
            }

            false
        } catch (ex: Exception) {
            Log.e(TAG, "Parse fail => block url=$url", ex)
            true
        }
    }
}
