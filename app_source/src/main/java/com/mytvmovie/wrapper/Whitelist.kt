package com.mytvmovie.wrapper

import android.content.Context
import android.util.Log

/**
 * Loads allowed domains from assets/whitelist_domains.txt
 * If a domain is not in the list => BLOCK IT.
 */
object Whitelist {
    private const val TAG = "Whitelist"
    private val allowedHosts = mutableSetOf<String>()

    fun load(context: Context) {
        allowedHosts.clear()
        try {
            val lines = context.assets.open("whitelist_domains.txt")
                .bufferedReader().readLines()
                .map { it.trim().lowercase() }
                .filter { it.isNotBlank() && !it.startsWith("#") }

            allowedHosts.addAll(lines)
            Log.i(TAG, "Loaded ${allowedHosts.size} whitelisted domains")
        } catch (ex: Exception) {
            Log.e(TAG, "FAILED to load whitelist_domains.txt", ex)
        }
    }

    fun isAllowed(host: String?): Boolean {
        if (host.isNullOrBlank()) return false
        val h = host.lowercase()

        // Exact match or subdomain match
        return allowedHosts.any { allowed ->
            h == allowed || h.endsWith(".$allowed")
        }
    }

    fun dump(): String = allowedHosts.joinToString(", ")
}
