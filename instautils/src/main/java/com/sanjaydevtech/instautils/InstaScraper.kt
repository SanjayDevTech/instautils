package com.sanjaydevtech.instautils;

import android.app.Activity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InstaScraper class for downloading dp
 */
public class InstaScraper {

    private static final String DP_URL = "^(https://(www\\.)?instagram\\.com/[0-9a-zA-Z_.]+)";
    private static final String NOISE = "\\u0026";
    private static final String PROFILE_HD_PATTERN = "\"profile_pic_url_hd\":\"([^\"]*)\"";
    private static final String PROFILE_PATTERN = "\"profile_pic_url\":\"([^\"]*)\"";


    InstaScraper() {

    }

    /**
     * To retrieve Instagram profile
     *
     * @param activity Current activity
     * @param url      Url of the instagram profile
     * @param response InstaResponse instance
     * @throws NullPointerException Throws if no InstaResponse is attached
     */

    public static void getDP(final Activity activity, final String url, final InstaResponse response) throws NullPointerException {
        if (response == null) {
            throw new NullPointerException("No InstaResponse Listener is attached");
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    boolean isData = false;
                    Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
                    Elements scripts = document.getElementsByTag("script");
                    for (Element script : scripts) {
                        if (isData) {
                            break;
                        }
                        for (DataNode dataNode : script.dataNodes()) {
                            String scriptData = dataNode.getWholeData().trim();
                            if (scriptData.startsWith("window._sharedData")) {
                                String profilePicHD = matchPattern(scriptData, PROFILE_HD_PATTERN);
                                String profilePic = matchPattern(scriptData, PROFILE_PATTERN);
                                if (profilePicHD != null) {
                                    profilePicHD = profilePicHD.replace(NOISE, "&");
                                }
                                if (profilePic != null) {
                                    profilePic = profilePic.replace(NOISE, "&");
                                }
                                final String finalProfilePicHD = profilePicHD;
                                final String finalProfilePic = profilePic;
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        response.onResponse(new InstaPost(finalProfilePicHD, InstaPost.INSTA_PROFILE, finalProfilePic));
                                    }
                                });
                                isData = true;
                                break;
                            }
                        }
                    }
                    if (!isData) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                response.onError(new NullPointerException("No data resource found"));
                            }
                        });
                    }

                } catch (final IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            response.onError(e);
                        }
                    });
                }

            }
        }.start();

    }

    private static String matchPattern(String data, String patTxt) {
        Pattern pattern = Pattern.compile(patTxt);
        Matcher matcher = pattern.matcher(data);
        boolean patMatch = matcher.find();
        if (!patMatch) {
            return null;
        }
        return matcher.group(1);
    }
}
