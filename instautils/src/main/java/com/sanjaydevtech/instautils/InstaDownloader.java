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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Downloader Class to download insta posts
 *
 * @author Sanjay Developer
 * @version 1.0.1
 */
public class InstaDownloader {
    private Activity activity;
    private InstaResponse response;
    private static final String TAG = InstaDownloader.class.getSimpleName();

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
                try {
                    Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                    Elements metas = document.getElementsByTag("meta");
                    String img = null, vid = null, type = null;
                    for (Element meta : metas) {
                        String property = meta.attr("property");
                        if (property.equals("og:image")) {
                            img = meta.attr("content");
                            continue;
                        }
                        if (property.equals("og:video")) {
                            vid = meta.attr("content");
                            continue;
                        }
                        if (property.equals("og:type")) {
                            type = meta.attr("content");
                        }
                    }
                    if (type == null || (!type.equals("video") && !type.equals("instapp:photo"))) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                response.onError(new NullPointerException("No data resource found"));
                            }
                        });
                    } else {
                        if (type.equals("instapp:photo")) {
                            if (img != null) {
                                final String finalImg = img;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        response.onResponse(new InstaPost(finalImg, InstaPost.INSTA_IMAGE, finalImg));
                                    }
                                });
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        response.onError(new NullPointerException("No data resource found"));
                                    }
                                });
                            }
                        } else {
                            if (img != null && vid != null) {
                                final String finalImg1 = img;
                                final String finalVid = vid;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        response.onResponse(new InstaPost(finalVid, InstaPost.INSTA_VIDEO, finalImg1));
                                    }
                                });
                            } else {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        response.onError(new NullPointerException("No data resource found"));
                                    }
                                });
                            }
                        }

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


    /**
     * Retrieve the bitmap of the image post or thumbnail of video post
     *
     * @param post       InstaPost object retrieved from InstaResponse
     * @param instaImage Listener to handle bitmap
     * @throws NullPointerException If InstaImage Listener is not attached
     */
    public void getBitmap(InstaPost post, final InstaImage instaImage) throws NullPointerException {
        if (instaImage == null) {
            throw new NullPointerException("No InstaImage listener attached");
        }
        String imgUrl = post.getThumbnailUrl();
        Glide.with(activity)
                .asBitmap()
                .load(imgUrl)
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
     */
    public void setImage(InstaPost post, ImageView imageView) {
        String imgUrl = post.getThumbnailUrl();
        Glide.with(activity)
                .load(imgUrl)
                .into(imageView);
    }
}
