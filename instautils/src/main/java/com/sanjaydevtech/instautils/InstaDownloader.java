package com.sanjaydevtech.instautils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * Downloader Class to download insta posts
 *
 * @author Sanjay Developer
 * @version 0.0.1
 */
public class InstaDownloader {
    private Context context;
    private InstaResponse response;

    /**
     * Public constructor
     *
     * @param context Context of the activity
     */
    public InstaDownloader(Context context) {
        this.context = context;
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
    public void get(String url) throws NullPointerException {
        if (response == null) {
            throw new NullPointerException("No InstaResponse Listener is attached");
        }
        //TODO to retrieve the image or video url
        response.onResponse(new InstaPost("", InstaPost.INSTA_IMAGE));
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
        Glide.with(context)
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
        Glide.with(context)
                .load(post.getUrl())
                .into(imageView);
    }
}
