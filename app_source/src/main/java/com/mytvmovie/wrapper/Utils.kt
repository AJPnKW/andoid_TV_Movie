package com.mytvmovie.wrapper

import android.content.Context
import android.util.Log
import org.json.JSONObject

object Utils {
    private const val TAG = "Utils"

    fun loadAssetText(context: Context, fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to read asset: $fileName", ex)
            ""
        }
    }

    fun loadParentSettings(context: Context): JSONObject {
        val txt = loadAssetText(context, "parent_mode_settings.json")
        return try {
            JSONObject(txt)
        } catch (ex: Exception) {
            Log.e(TAG, "Invalid parent_mode_settings.json", ex)
            JSONObject()
        }
    }

    fun forcedUserAgent(context: Context): String {
        val ua = loadAssetText(context, "user_agent.txt").trim()
        return if (ua.isBlank()) {
            // fallback UA if file missing
            "Mozilla/5.0 (Linux; Android 14; Pixel 9) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"
        } else ua
    }
}
