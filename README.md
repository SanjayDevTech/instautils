# Insta Utils

Insta Utils is a Insta post downloader for Android applications written in **Java**.

[![Build Status](https://travis-ci.org/SanjayDevTech/instautils.svg?branch=master)](https://travis-ci.org/SanjayDevTech/instautils)
[ ![Download](https://api.bintray.com/packages/sanjaydevtech/instautils/com.sanjaydevtech.instautils/images/download.svg) ](https://bintray.com/sanjaydevtech/instautils/com.sanjaydevtech.instautils/_latestVersion)

# New Features!
Check [CHANGELOG](https://github.com/SanjayDevTech/instautils/blob/master/CHANGELOG.md) for new features and updates


### Credits

Insta Utils uses a number of open source libraries to work properly:

* JSoup - To parse HTML
* [Glide](https://github.com/bumptech/glide) - Url to ImageView

### Implementation
```gradle
implementation 'com.sanjaydevtech.instautils:instautils:<latest_version>'
```
Check [Releases](https://github.com/SanjayDevTech/instautils/releases) for Latest version


### Instagram DP

#### Fetching the url
```java
String instaProfile = "https://instagram.com/SanjayDevTech";
InstaScraper.getDP(this, instaProfile, new InstaResponse() {
    @Override
    public void onResponse(InstaPost post) {
        int type = post.getType(); // InstaPost.INSTA_PROFILE
        String url = post.getUrl();
        String thumbUrl = post.getThumbnailUrl();
    }
    @Override
    public void onError(Exception e) {
    	Log.d("MainActivity", "Exception => "+e);
    }
});
```

#### Displaying

After retrieving the InstaPost object you can set the image to an ImageView
```java
InstaDownloader downloader = new InstaDownloader(this);
ImageView img = findViewById(R.id.imageView);
downloader.setImage(post, img);
```
Or you can get a Bitmap object
```java
downloader.getBitmap(post, new InstaImage() {
    @Override
    public void onBitmapLoaded(Bitmap bitmap) {
        ImageView img = findViewById(R.id.imageView);
        img.setImageBitmap(bitmap);
    }
});
```


### Image or Video Post

#### Initialisation

```java
InstaDownloader downloader = new InstaDownloader(this);
downloader.setResponse(new InstaResponse() {
    @Override
    public void onResponse(InstaPost post) {
        int type = post.getType(); // InstaPost.INSTA_IMAGE or InstaPost.INSTA_VIDEO
        String url = post.getUrl();
        String thumbUrl = post.getThumbnailUrl();
    }
    @Override
    public void onError(Exception e) {
    	Log.d("MainActivity", "Exception => "+e);
    }
});
```

#### Requesting a Post

```java
String sampleUrl = "https://www.instagram.com/p/123456";
downloader.get(sampleUrl);
```

#### Setting image
After retrieving the InstaPost object you can set the image to an ImageView
```java
ImageView img = findViewById(R.id.imageView);
downloader.setImage(post, img);
```
Or you can get a Bitmap object
```java
downloader.getBitmap(post, new InstaImage() {
    @Override
    public void onBitmapLoaded(Bitmap bitmap) {
        ImageView img = findViewById(R.id.imageView);
        img.setImageBitmap(bitmap);
    }
});
```

### Want to contribute?
* Fork this repository.
* Clone to your local machine
```sh
git clone https://github.com/SanjayDevTech/instautils.git
```
* After doing your stuff
```sh
git add *
git commit -m "Commit message"
git push
```
* Then create a new pull request on github

### Any Issues?
* Create a new issue on github

License
----
MIT
