package com.sanjaydevtech.instarepost;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sanjaydevtech.instautils.InstaDownloader;
import com.sanjaydevtech.instautils.InstaPost;
import com.sanjaydevtech.instautils.InstaResponse;
import com.sanjaydevtech.instautils.InstaScraper;
import com.sanjaydevtech.instautils.InstaTask;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondActivity extends AppCompatActivity {

    private InstaDownloader downloader; // Declare the InstaDownloader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL_PATTERN = "^https://www.instagram.com/p/.+";
    private static final String DP_URL_PATTERN = "^(https://(www\\.)?instagram\\.com/[^p][0-9a-zA-Z_.]+)";
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        downloader = new InstaDownloader(this, instaTask ->
                onResponseMethod(instaTask)
        );

        final EditText urlTxt = findViewById(R.id.urlTxt);
        final Button doneBtn = findViewById(R.id.doneBtn);
        img = findViewById(R.id.imageView);

        doneBtn.setOnClickListener(view -> {
            Pattern pattern = Pattern.compile(URL_PATTERN);
            Matcher matcher = pattern.matcher(urlTxt.getText().toString());
            if (matcher.find()) {
                downloader.get(urlTxt.getText().toString()); // Request the post data
            } else {
                pattern = Pattern.compile(DP_URL_PATTERN);
                matcher = pattern.matcher(urlTxt.getText().toString());
                if (matcher.find()) {
                    InstaScraper.getDP(SecondActivity.this, urlTxt.getText().toString(), instaTask ->
                            onResponseMethod(instaTask)
                    );
                } else {
                    Toast.makeText(SecondActivity.this, "Invalid insta url", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void onResponseMethod(@NotNull InstaTask instaTask) {
        InstaPost instaPost = instaTask.getInstaPost();
        if (instaPost != null) {
            Log.d(TAG, instaPost.getUrl());
            // Retrieve the post object after request
            downloader.setImage(instaPost, img);
            Log.d(TAG, "Type: " + instaPost.getType());
        } else {
            instaTask.getException().printStackTrace();
        }
    }

}