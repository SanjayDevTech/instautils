# Insta Utils

Insta Utils is a Insta post downloader for Android applications written in **Java**.

[![Build Status](https://travis-ci.com/SanjayDevTech/instautils.svg?branch=master)](https://travis-ci.com/SanjayDevTech/instautils)
[![Download](https://api.bintray.com/packages/sanjaydevtech/instautils/com.sanjaydevtech.instautils/images/download.svg) ](https://bintray.com/sanjaydevtech/instautils/com.sanjaydevtech.instautils/_latestVersion)

## New Features!
Check [CHANGELOG](https://github.com/SanjayDevTech/instautils/blob/master/CHANGELOG.md) for new features and updates


## Credits

Insta Utils uses a number of open source libraries to work properly:

- JSoup - To parse HTML
- [Glide](https://github.com/bumptech/glide) - Url to ImageView

## Implementation
```gradle
implementation 'com.sanjaydevtech.instautils:instautils:<latest_version>'
```
Check [Releases](https://github.com/SanjayDevTech/instautils/releases) for Latest version


## Usage

### Initialize InstaDownloader
```java
InstaDownloader downloader = new InstaDownloader(this, new InstaResponse() {
    @Override
    public void onResponse(@NotNull InstaTask instaTask) {
        // Retrieve post instance from InstaTask
        InstaPost instaPost = instaTask.getInstaPost();

        if (instaPost != null) {
            // InstaPost has something in it

            // Log the direct url
            Log.d(TAG, instaPost.getUrl());

            // set the post object to ImageView
            downloader.setImage(instaPost, displayImageView);

            // Log the type (0 => Image, 1 => Video or 2 => Profile)
            Log.d(TAG, "Type: " + instaPost.getType());
        } else {
            // Log the error
            instaTask.getException().printStackTrace();
        }
    }
});
```


### Instagram profile
```java
String instaProfileUrl = "https://instagram.com/SanjayDevTech";
downloader.getDP(instaProfileUrl);
```

### Instagram Image/Video
```java
String instaPostUrl = "https://www.instagram.com/p/post_id/";
downloader.get(instaPostUrl);
```


## Any Issues?
* Create a new issue on github
* Pull requests are welcomed 😀


## License
----
MIT
