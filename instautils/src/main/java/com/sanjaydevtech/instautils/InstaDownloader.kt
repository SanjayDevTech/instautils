package com.sanjaydevtech.instautils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

/**
 * Downloader Class to download insta posts
 *
 * @author Sanjay Developer
 * @version 1.1.0
 */
class InstaDownloader(private val context: Context?, private val scope: CoroutineScope, private val response: InstaResponse) {

    constructor(activity: FragmentActivity, response: InstaResponse) : this(activity, activity.lifecycleScope, response)
    constructor(fragment: Fragment, response: InstaResponse) : this(fragment.context, fragment.viewLifecycleOwner.lifecycleScope, response)

    /**
     * Get the url of the post
     *
     * @param url URL of that post
     */
    fun get(url: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).userAgent("Mozilla/5.0").get()
                val metas = document.getElementsByTag("meta")
                var img: String? = null
                var vid: String? = null
                var type: String? = null
                for (meta in metas) {
                    val property = meta.attr("property")
                    if (property == "og:image") {
                        img = meta.attr("content")
                        continue
                    }
                    if (property == "og:video") {
                        vid = meta.attr("content")
                        continue
                    }
                    if (property == "og:type") {
                        type = meta.attr("content")
                    }
                }
                if (type == null || type != "video" && type != "instapp:photo") {
                    withContext(Dispatchers.Main) { response.onResponse(InstaTask(null, IllegalArgumentException("No data resource found"))) }
                } else {
                    if (type == "instapp:photo") {
                        if (img != null) {
                            withContext(Dispatchers.Main) { response.onResponse(InstaTask(InstaPost(img, InstaPost.INSTA_IMAGE, img), null)) }
                        } else {
                            withContext(Dispatchers.Main) { response.onResponse(InstaTask(null, IllegalArgumentException("No data resource found"))) }
                        }
                    } else {
                        if (img != null && vid != null) {
                            withContext(Dispatchers.Main) { response.onResponse(InstaTask(InstaPost(vid, InstaPost.INSTA_VIDEO, img), null)) }
                        } else {
                            withContext(Dispatchers.Main) { response.onResponse(InstaTask(null, IllegalArgumentException("No data resource found"))) }
                        }
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) { response.onResponse(InstaTask(null, e)) }
            }
        }
    }

    /**
     * A Kotlin Extension function to set image
     *
     * @param post InstaPost instance
     */
    fun ImageView.setImage(post: InstaPost) {
        setImage(post, this)
    }

    /**
     * Retrieve the bitmap of the image post or thumbnail of video post
     *
     * @param post       InstaPost object retrieved from InstaResponse
     * @param instaImage Listener to handle bitmap
     */
    fun getBitmap(post: InstaPost, instaImage: InstaImage) {
        val imgUrl = post.thumbnailUrl
        context?.let {
            Glide.with(it)
                    .asBitmap()
                    .load(imgUrl)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                            instaImage.onBitmapLoaded(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
        } ?: run {
            Log.e(TAG, "method: getBitmap can't set image.. [REASON] Context is null")
        }
    }

    /**
     * Post to ImageView
     *
     * @param post      InstaPost object
     * @param imageView To which view that image has to set
     */
    fun setImage(post: InstaPost, imageView: ImageView) {
        val imgUrl = post.thumbnailUrl
        context?.let {
            Glide.with(it)
                    .load(imgUrl)
                    .into(imageView)
        } ?: run {
            Log.e(TAG, "method: setImage can't set image.. [REASON] Context is null")
        }
    }

    companion object {
        private val TAG = InstaDownloader::class.simpleName
    }
}