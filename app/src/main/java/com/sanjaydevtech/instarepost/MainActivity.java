package com.sanjaydevtech.instarepost;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.sanjaydevtech.instautils.InstaDownloader;
import com.sanjaydevtech.instautils.InstaImage;
import com.sanjaydevtech.instautils.InstaPost;
import com.sanjaydevtech.instautils.InstaResponse;

public class MainActivity extends AppCompatActivity implements InstaResponse {

    private InstaDownloader downloader; // Declare the InstaDownloader
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String sampleUrl = "https://www.instagram.com/p/123";
        downloader = new InstaDownloader(this); // Initialise the downloader
        downloader.setResponse(this); // Set the response listener
        downloader.get(sampleUrl); // Request the post data
    }

    @Override
    public void onResponse(InstaPost post) {
        // Retrieve the post object after request
        downloader.getBitmap(post, new InstaImage() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap) {
                // Here set the ImageView to bitmap like that things
            }
        });
    }

    @Override
    public void onError(Exception e) {
        // if any exception occurs like Private post
        Log.d(TAG, "Exception: " + e.getMessage());
    }
}