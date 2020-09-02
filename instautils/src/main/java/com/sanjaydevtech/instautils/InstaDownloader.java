package com.sanjaydevtech.instautils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Downloader Class to download insta posts
 *
 * @author Sanjay Developer
 * @version 1.0.0
 */
public class InstaDownloader {
    private Activity activity;
    private InstaResponse response;
    private static final String TAG = InstaDownloader.class.getSimpleName();
    private static final String IMAGE_PATTERN = "\"src\":\"([^\"]*)\"";
    private static final String VIDEO_PATTERN = "\"video_url\":\"([^\"]*)\"";
    private static final String NOISE = "\\u0026";

    /**
     * Public constructor
     *
     * @param activity Context of the activity
     */
    public InstaDownloader(Activity activity) {
        this.activity = activity;
    }

    /**
     * To attach the InstaResponse Listener
     *
     * @param response Attach the InstaResponse Listener
     */
    public void setResponse(InstaResponse response) {
        this.response = response;
    }

    /**
     * Get the url of the post
     *
     * @param url URL of that post
     * @throws NullPointerException NullPointerException is thrown if InstaResponse is not attached
     */
    public void get(final String url) throws NullPointerException {
        if (response == null) {
            throw new NullPointerException("No InstaResponse Listener is attached");
        }
        new Thread() {
            public void run() {
                boolean isData = false;
                try {
                    Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                    Elements scripts = document.getElementsByTag("script");
                    for (Element script : scripts) {
                        if (isData) {
                            break;
                        }
                        for (DataNode node : script.dataNodes()) {
                            String url = matchPattern(node.getWholeData(), IMAGE_PATTERN);
                            if (url != null) {
                                url = url.replace(NOISE, "&");
                                final String finalUrl = url;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        response.onResponse(new InstaPost(finalUrl, InstaPost.INSTA_IMAGE));
                                    }
                                });
                                isData = true;
                                break;
                            }
                        }
                    }
                    if (!isData) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                response.onError(new NullPointerException("No data resource found"));
                            }
                        });
                    }

                } catch (final IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            response.onError(e);
                        }
                    });
                }

            }


        }.start();
    }

    private String matchPattern(String data, String patTxt) {
        Pattern pattern = Pattern.compile(patTxt);
        Matcher matcher = pattern.matcher(data);
        boolean patMatch = matcher.find();
        if (!patMatch) {
            return null;
        }
        return matcher.group(1);
    }

    /**
     * Retrieve the bitmap of the image post
     *
     * @param post       InstaPost object retrieved from InstaResponse
     * @param instaImage Listener to handle bitmap
     * @throws NullPointerException     If InstaImage Listener is not attached
     * @throws IllegalArgumentException If the passed Post is not an image
     */
    public void getBitmap(InstaPost post, final InstaImage instaImage) throws NullPointerException, IllegalArgumentException {
        if (instaImage == null) {
            throw new NullPointerException("No InstaImage listener attached");
        }
        if (post.getType() != InstaPost.INSTA_IMAGE) {
            throw new IllegalArgumentException("Given InstaPost is not an Image");
        }
        Glide.with(activity)
                .asBitmap()
                .load(post.getUrl())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        instaImage.onBitmapLoaded(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    /**
     * Post to ImageView
     *
     * @param post      InstaPost object
     * @param imageView To which view that image has to set
     * @throws IllegalArgumentException Thrown if InstaPost is not an Image
     */
    public void setImage(InstaPost post, ImageView imageView) throws IllegalArgumentException {
        if (post.getType() != InstaPost.INSTA_IMAGE) {
            throw new IllegalArgumentException("Given InstaPost is not an image");
        }
        Glide.with(activity)
                .load(post.getUrl())
                .into(imageView);
    }
}
