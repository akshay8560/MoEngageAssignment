package com.example.moengageassignment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import java.net.URLDecoder


class WebViewActivity : AppCompatActivity() {

    companion object {
        const val URL_EXTRA = "url"

    }

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web_view)

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar2)

        val encodedUrl = intent.getStringExtra(URL_EXTRA)
        val url = URLDecoder.decode(encodedUrl, "UTF-8")
        if (!url.isNullOrBlank()) {
            openWebView(url)
        } else {
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebView(url: String) {
        var newUrl = ""
        val httpId = "http://"
        val httpsId = "https://"
        if (url.startsWith(httpId)) {
            newUrl = url.replace(httpId, httpsId)
        }
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.settings.domStorageEnabled = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        webView.loadUrl(newUrl)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }
        }

    }
}
