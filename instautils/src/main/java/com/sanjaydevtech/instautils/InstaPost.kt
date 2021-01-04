package com.sanjaydevtech.instautils

/**
 * InstaPost Object
 */
data class InstaPost internal constructor(
        val url: String,
        val type: Int,
        val thumbnailUrl: String) {

    companion object {
        const val INSTA_IMAGE = 0
        const val INSTA_VIDEO = 1
        const val INSTA_PROFILE = 2
    }
}