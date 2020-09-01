package com.sanjaydevtech.instautils;

import android.graphics.Bitmap;

/**
 * InstaImage
 */
public interface InstaImage {

    /**
     * OnBitmap loaded
     *
     * @param bitmap bitmap from a image post
     */
    void onBitmapLoaded(Bitmap bitmap);
}
