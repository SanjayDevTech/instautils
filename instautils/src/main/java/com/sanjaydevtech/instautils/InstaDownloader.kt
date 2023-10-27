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
import java.util.regex.Pattern

/**
 * Downloader Class to download insta posts
 *
 * @author Sanjay Developer
 * @version 1.2.0
 */
class InstaDownloader internal constructor(private val context: Context?, private val scope: CoroutineScope, private val response: InstaResponse) {

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
                e.printStackTrace()
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

    /**
     * To retrieve Instagram profile
     *
     * @param url      Url of the instagram profile
     */
    fun getDP(url: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).userAgent("Mozilla/5.0").get()
                val scripts = document.getElementsByTag("script")
                for (script in scripts) {
                    for (dataNode in script.dataNodes()) {
                        val scriptData = dataNode.wholeData.trim { it <= ' ' }
                        if (scriptData.startsWith("window._sharedData")) {
                            var profilePicHD = matchPattern(scriptData, PROFILE_HD_PATTERN)
                            var profilePic = matchPattern(scriptData, PROFILE_PATTERN)
                            if (profilePicHD != null) {
                                profilePicHD = profilePicHD.replace(NOISE, "&")
                            }
                            if (profilePic != null) {
                                profilePic = profilePic.replace(NOISE, "&")
                            }
                            withContext(Dispatchers.Main) { response.onResponse(InstaTask(InstaPost(profilePicHD!!, InstaPost.INSTA_PROFILE, profilePic!!), null)) }
                            return@launch
                        }
                    }
                }
                withContext(Dispatchers.Main) { response.onResponse(InstaTask(null, IllegalArgumentException("No data resource found"))) }

            } catch (e: IOException) {
                withContext(Dispatchers.Main) { response.onResponse(InstaTask(null, e)) }
            }
        }
    }

    private fun matchPattern(data: String, patTxt: String): String? {
        val pattern = Pattern.compile(patTxt)
        val matcher = pattern.matcher(data)
        val patMatch = matcher.find()
        return if (!patMatch) {
            null
        } else matcher.group(1)
    }

    companion object {
        private val TAG = InstaDownloader::class.simpleName
        private const val DP_URL = "^(https://(www\\.)?instagram\\.com/[0-9a-zA-Z_.]+)"
        private const val NOISE = "\\u0026"
        private const val PROFILE_HD_PATTERN = "\"profile_pic_url_hd\":\"([^\"]*)\""
        private const val PROFILE_PATTERN = "\"profile_pic_url\":\"([^\"]*)\""
    }
}