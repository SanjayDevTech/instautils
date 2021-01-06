package com.sanjaydevtech.instautils

/**
 * InstaPost Object
 *
 * [url] holds the actual image or video url
 *
 * [type] may be constants [INSTA_IMAGE] or [INSTA_VIDEO] or [INSTA_PROFILE]
 *
 * [thumbnailUrl] is the same as url for type [INSTA_IMAGE],
 * but a low res image for [INSTA_VIDEO] and [INSTA_PROFILE]
 */
data class InstaPost internal constructor(
        val url: String,
        val type: Int,
        val thumbnailUrl: String) {

    companion object {
        /** InstaPost object is from a Image */
        const val INSTA_IMAGE = 0

        /** InstaPost object is from a Video */
        const val INSTA_VIDEO = 1

        /** InstaPost object is from a Instagram Profile */
        const val INSTA_PROFILE = 2
    }
}