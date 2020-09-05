package com.sanjaydevtech.instautils;

/**
 * InstaPost Object
 */
public class InstaPost {
    private String url;
    private int type;
    private String thumbnailUrl;
    public static final int INSTA_IMAGE = 0;
    public static final int INSTA_VIDEO = 1;

    InstaPost(String url, int type, String thumbnailUrl) {
        this.url = url;
        this.type = type;
        this.thumbnailUrl = thumbnailUrl;
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
     * get the thumbnail
     *
     * @return a String
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
