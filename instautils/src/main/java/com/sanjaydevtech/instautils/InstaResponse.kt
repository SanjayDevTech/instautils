package com.sanjaydevtech.instautils;

/**
 * InstaResponse to handle download url searching
 */
public interface InstaResponse {
    /**
     * On successful url searching
     *
     * @param post InstaPost object
     */
    void onResponse(InstaPost post);

    /**
     * When an error occurs
     *
     * @param e Exception
     */
    void onError(Exception e);
}
