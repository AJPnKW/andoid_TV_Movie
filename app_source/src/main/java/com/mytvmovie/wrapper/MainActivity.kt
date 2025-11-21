package com.mytvmovie.wrapper

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

/*
 * Script/Program Name: MainActivity.kt
 * Purpose: Parent-safe Android TV wrapper for my_TV_Movie website
 * Author: Andrew J. Pearen
 * Created: 2025-11-21
 * Last Update: 2025-11-21
 *
 * Run command (Android Studio):
 *  - Build -> Build APK(s)
 */

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var webView: WebView
    private lateinit var errorLayer: LinearLayout
    private lateinit var retryBtn: Button

    private lateinit var settings: JSONObject
    private lateinit var startUrl: String
    private lateinit var forcedUA: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // UI refs
        webView = findViewById(R.id.webview)
        errorLayer = findViewById(R.id.error_layer)
        retryBtn = findViewById(R.id.retry_btn)

        retryBtn.setOnClickListener {
            hideError()
            webView.reload()
        }

        // Load configs
        Whitelist.load(this)
        settings = Utils.loadParentSettings(this)
        startUrl = settings.optString("start_url", "https://example.com")
        forcedUA = Utils.forcedUserAgent(this)

        Log.i(TAG, "Start URL: $startUrl")
        Log.i(TAG, "Whitelist: ${Whitelist.dump()}")
        Log.i(TAG, "Forced UA: $forcedUA")

        // WebView setup
        val ws = webView.settings
        ws.javaScriptEnabled = true
        ws.domStorageEnabled = true
        ws.databaseEnabled = true
        ws.mediaPlaybackRequiresUserGesture = false
        ws.loadsImagesAutomatically = true
        ws.useWideViewPort = true
        ws.loadWithOverviewMode = true
        ws.builtInZoomControls = false
        ws.displayZoomControls = false
        ws.userAgentString = forcedUA

        // Required for some hosts
        ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        // Kill popups by refusing new windows
        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: android.os.Message?
            ): Boolean {
                Log.w(TAG, "Popup window blocked")
                return false
            }
        }

        webView.webViewClient = SafeWebViewClient { err ->
            showError(err)
        }

        // Load home
        webView.loadUrl(startUrl)
        webView.requestFocus()
    }

    private fun showError(details: String) {
        Log.e(TAG, "SHOW ERROR: $details")
        errorLayer.visibility = View.VISIBLE
        retryBtn.requestFocus()
    }

    private fun hideError() {
        errorLayer.visibility = View.GONE
        webView.requestFocus()
    }

    // Back button: go back in WebView if possible, else exit
    override fun onBackPressed() {
        if (errorLayer.visibility == View.VISIBLE) {
            hideError()
            return
        }
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    // DPAD support: let WebView handle navigation
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return super.dispatchKeyEvent(event)
    }
}
