package com.sanjaydevtech.instautils

/**
 * InstaResponse to handle download url searching
 */
fun interface InstaResponse {
    /**
     * On completion of url searching
     *
     * @param instaTask InstaTask object which holds the post or exception
     */
    fun onResponse(instaTask: InstaTask)
}