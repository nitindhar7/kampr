package com.nitindhar.kampr.async;

import java.util.concurrent.Callable;

import android.graphics.Bitmap;

import com.nitindhar.kampr.util.ImageUtils;

public class PostSnapFetchTask implements Callable<Bitmap> {

    private final String url;

    public PostSnapFetchTask(String url) {
        this.url = url;
    }

    @Override
    public Bitmap call() throws Exception {
        return ImageUtils.fetchImageBitmap(url);
    }

}