package com.sanjaydevtech.instarepost;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sanjaydevtech.instautils.InstaDownloader;
import com.sanjaydevtech.instautils.InstaImage;
import com.sanjaydevtech.instautils.InstaPost;
import com.sanjaydevtech.instautils.InstaResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements InstaResponse {

    private InstaDownloader downloader; // Declare the InstaDownloader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL_PATTERN = "^https://www.instagram.com/p/.+";
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloader = new InstaDownloader(this); // Initialise the downloader
        downloader.setResponse(this); // Set the response listener
        final EditText urlTxt = findViewById(R.id.urlTxt);
        Button doneBtn = findViewById(R.id.doneBtn);
        img = findViewById(R.id.imageView);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pattern pattern = Pattern.compile(URL_PATTERN);
                Matcher matcher = pattern.matcher(urlTxt.getText().toString());
                if (matcher.find()) {
                    downloader.get(urlTxt.getText().toString()); // Request the post data
                } else {
                    Toast.makeText(MainActivity.this, "Invalid insta url", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResponse(InstaPost post) {
        Log.d(TAG, post.getUrl());
        // Retrieve the post object after request
        downloader.setImage(post, img);
        Log.d(TAG, "Type: "+post.getType());
    }

    @Override
    public void onError(Exception e) {
        // if any exception occurs like Private post
        e.printStackTrace();
    }
}