package com.sanjaydevtech.instautils;

/**
 * InstaPost Object
 */
public class InstaPost {
    private String url;
    private int type;
    private String originalUrl;
    public static final int INSTA_IMAGE = 0;
    public static final int INSTA_VIDEO = 1;

    InstaPost(String url, int type, String originalUrl) {
        this.url = url;
        this.type = type;
        this.originalUrl = originalUrl;
    }

    /**
     * get the post download url
     *
     * @return a String
     */
    public String getUrl() {
        return url;
    }

    /**
     * get the type
     *
     * @return a int
     */
    public int getType() {
        return type;
    }

    /**
     * get he original post url
     *
     * @return a String of post url
     */
    public String getOriginalUrl() {
        return originalUrl;
    }
}
