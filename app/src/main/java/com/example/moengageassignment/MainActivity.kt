package com.example.moengageassignment

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    private lateinit var articleListAdapter: ArticleListAdapter

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Firebase.messaging.isAutoInitEnabled = true
        FirebaseMessaging.getInstance().subscribeToTopic("news")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscription failed"
                }
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }


            val token = task.result


            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        articleListAdapter = ArticleListAdapter { url ->
            openInAppBrowser(url)
        }

        recyclerview.layoutManager = LinearLayoutManager(this)
        val apiUrl = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
        apiCall(apiUrl, progressBar)
        recyclerview.adapter = articleListAdapter
    }

    private fun apiCall(apiUrl: String, progressBar: ProgressBar){
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val url: URL = URL(apiUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                // Response code
                val responseCode: Int = connection.responseCode
                println("Response Code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader: BufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.use { it.readText() }

                    reader.close()

                    val responseObject = Gson().fromJson(response, ApiResponse::class.java)

                    val articleData = responseObject.articles
                    if (!articleData.isNullOrEmpty()) {
                        runOnUiThread{
                            articleListAdapter.submitList(articleData)
                            progressBar.visibility = View.GONE
                        }
                    }
                } else {
                    println("Error: Unable to fetch data from the API")
                }

                connection.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openInChromeBrowser(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
    private fun openInAppBrowser(url: String) {
        val encodedUrl = URLEncoder.encode(url, "UTF-8")
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.URL_EXTRA, encodedUrl)
        startActivity(intent)
    }
}