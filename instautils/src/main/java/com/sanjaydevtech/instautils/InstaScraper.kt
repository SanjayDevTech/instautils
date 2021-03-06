package com.sanjaydevtech.instautils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

/**
 * InstaScraper class for downloading dp
 */
@Deprecated("Use InstaDownloader.getDP()")
object InstaScraper {
    private const val DP_URL = "^(https://(www\\.)?instagram\\.com/[0-9a-zA-Z_.]+)"
    private const val NOISE = "\\u0026"
    private const val PROFILE_HD_PATTERN = "\"profile_pic_url_hd\":\"([^\"]*)\""
    private const val PROFILE_PATTERN = "\"profile_pic_url\":\"([^\"]*)\""

    /**
     * To retrieve Instagram profile
     *
     * @param activity Activity
     * @param url      Url of the instagram profile
     * @param response InstaResponse instance
     */
    @JvmStatic
    fun getDP(activity: FragmentActivity, url: String, response: InstaResponse) {
        getDP(activity.lifecycleScope, url, response)
    }

    /**
     * To retrieve Instagram profile
     *
     * @param fragment Fragment
     * @param url      Url of the instagram profile
     * @param response InstaResponse instance
     */
    @JvmStatic
    fun getDP(fragment: Fragment, url: String, response: InstaResponse) {
        getDP(fragment.viewLifecycleOwner.lifecycleScope, url, response)
    }


    @JvmStatic
    private fun getDP(scope: CoroutineScope, url: String, response: InstaResponse) {
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
}