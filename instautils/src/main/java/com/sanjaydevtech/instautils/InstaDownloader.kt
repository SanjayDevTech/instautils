package com.sanjaydevtech.instautils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException

/**
 * Downloader Class to download insta posts
 *
 * @author Sanjay Developer
 * @version 1.0.2
 */
class InstaDownloader(private val activity: FragmentActivity, private val response: InstaResponse) {

    constructor(activity: FragmentActivity, response: (InstaTask) -> Unit) : this(activity, object : InstaResponse {
        override fun onResponse(instaTask: InstaTask) {
            response(instaTask)
        }
    })

    /**
     * Get the url of the post
     *
     * @param url URL of that post
     */
    fun get(url: String) {
        activity.lifecycleScope.launch(Dispatchers.IO) {
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
     * Retrieve the bitmap of the image post or thumbnail of video post
     *
     * @param post       InstaPost object retrieved from InstaResponse
     * @param instaImage Listener to handle bitmap
     */
    fun getBitmap(post: InstaPost, instaImage: InstaImage) {
        val imgUrl = post.thumbnailUrl
        Glide.with(activity)
                .asBitmap()
                .load(imgUrl)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                        instaImage.onBitmapLoaded(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
    }

    /**
     * Post to ImageView
     *
     * @param post      InstaPost object
     * @param imageView To which view that image has to set
     */
    fun setImage(post: InstaPost, imageView: ImageView) {
        val imgUrl = post.thumbnailUrl
        Glide.with(activity)
                .load(imgUrl)
                .into(imageView)
    }

    companion object {
        private val TAG = InstaDownloader::class.java.simpleName
    }
}