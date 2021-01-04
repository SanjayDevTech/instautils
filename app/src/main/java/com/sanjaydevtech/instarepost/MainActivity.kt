package com.sanjaydevtech.instarepost

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sanjaydevtech.instautils.InstaDownloader
import com.sanjaydevtech.instautils.InstaResponse
import com.sanjaydevtech.instautils.InstaScraper
import com.sanjaydevtech.instautils.InstaTask
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var downloader: InstaDownloader
    private lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        downloader = InstaDownloader(this) { instaTask ->
            onResponse(instaTask)
        }

        val urlTxt: EditText = findViewById(R.id.urlTxt)
        val doneBtn: Button = findViewById(R.id.doneBtn)
        val testBtn: Button = findViewById(R.id.test_btn)
        img = findViewById(R.id.imageView)

        testBtn.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        doneBtn.setOnClickListener {
            var pattern = Pattern.compile(URL_PATTERN)
            var matcher = pattern.matcher(urlTxt.text.toString())
            if (matcher.find()) {
                downloader.get(urlTxt.text.toString()) // Request the post data
            } else {
                pattern = Pattern.compile(DP_URL_PATTERN)
                matcher = pattern.matcher(urlTxt.text.toString())
                if (matcher.find()) {
                    InstaScraper.getDP(this@MainActivity, urlTxt.text.toString()) { instaTask ->
                        onResponse(instaTask)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Invalid insta url", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onResponse(instaTask: InstaTask) {
        val instaPost = instaTask.instaPost
        if (instaPost != null) {
            Log.d(TAG, instaPost.url)
            // Retrieve the post object after request
            downloader.setImage(instaPost, img)
            Log.d(TAG, "Type: " + instaPost.type)
        } else {
            instaTask.exception!!.printStackTrace()
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val URL_PATTERN = "^https://www.instagram.com/p/.+"
        private const val DP_URL_PATTERN = "^(https://(www\\.)?instagram\\.com/[^p][0-9a-zA-Z_.]+)"
    }
}