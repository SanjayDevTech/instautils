package com.sanjaydevtech.instautils

import android.graphics.Bitmap

/** InstaImage
 */
fun interface InstaImage {
    /**
     * OnBitmap loaded
     *
     * @param bitmap bitmap from a image post
     */
    fun onBitmapLoaded(bitmap: Bitmap)
}