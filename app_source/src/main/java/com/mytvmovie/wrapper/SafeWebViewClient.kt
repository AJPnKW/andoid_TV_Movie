package com.mytvmovie.wrapper

import android.graphics.Bitmap
import android.util.Log
import android.webkit.*

/**
 * WebView client:
 * - Blocks popups/redirects/ads
 * - Allows only whitelisted hosts
 * - Logs everything for debugging
 */
class SafeWebViewClient(
    private val onError: (String) -> Unit
) : WebViewClient() {

    companion object {
        private const val TAG = "SafeWebViewClient"
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString()
        if (RequestInterceptor.shouldBlock(url)) {
            Log.w(TAG, "Override BLOCK $url")
            return true
        }
        Log.i(TAG, "Override ALLOW $url")
        return false
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        Log.i(TAG, "Page started: $url")
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        Log.i(TAG, "Page finished: $url")
        super.onPageFinished(view, url)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        val msg = "Web error ${error?.errorCode}: ${error?.description}"
        Log.e(TAG, msg)
        onError(msg)
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        val msg = "HTTP error ${errorResponse?.statusCode}"
        Log.e(TAG, msg)
        onError(msg)
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        val url = request?.url?.toString()
        if (RequestInterceptor.shouldBlock(url)) {
            // Return empty response to kill the request silently
            return WebResourceResponse("text/plain", "utf-8", null)
        }
        return super.shouldInterceptRequest(view, request)
    }
}
