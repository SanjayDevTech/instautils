# Insta Utils

Insta Utils is a Insta post downloader for Android applications written in **Java**.

[ ![Download](https://api.bintray.com/packages/sanjaydevtech/instautils/com.sanjaydevtech.instautils/images/download.svg) ](https://bintray.com/sanjaydevtech/instautils/com.sanjaydevtech.instautils/_latestVersion)

# New Features!

  - Download the Insta public post
  - Set the image directly to ImageView

### Credits

Insta Utils uses a number of open source libraries to work properly:

* JSoup - To parse HTML
* [Glide](https://github.com/bumptech/glide) - Url to ImageView

### Implemetation

```gradle
implementation 'com.sanjaydevtech.instautils:instautils:1.0.1'
```

### Initialisation

```java
InstaDownloader downloader = new InstaDowloader(this);
downloader.setResponse(new InstaResponse() {
    @Override
    public void onResponse(InstaPost post) {
        int type = post.getType(); // InstaPost.INSTA_IMAGE or InstaPost.INSTA_VIDEO
        String url = post.getUrl();
        String thumbUrl = post.getThumbnailUrl();
    }
    @Override
    public void onError(Exception e) {}
});
```

### Requesting a Post

```java
String sampleUrl = "https://www.instagram.com/p/123456";
downloader.get(sampleUrl);
```

### Setting image
After retrieving the InstaPost object you can set the image to any ImageView
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

### Todos

 - Find hidden bugs
 - Add insta video downloading functionality

License
----
MIT
