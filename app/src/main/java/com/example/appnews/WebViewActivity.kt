package com.example.appnews

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_view)
        val url=intent.getStringExtra("url")
        val webView:WebView=findViewById(R.id.webview)
        webView.webViewClient= WebViewClient()
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}