package com.sanjaydevtech.instautils;

/**
 * InstaPost Object
 */
public class InstaPost {
    private String url;
    private int type;
    public static final int INSTA_IMAGE = 0;
    public static final int INSTA_VIDEO = 1;

    InstaPost(String url, int type) {
        this.url = url;
        this.type = type;
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
}
