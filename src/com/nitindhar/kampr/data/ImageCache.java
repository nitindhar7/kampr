package com.nitindhar.kampr.data;

import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class ImageCache {

    private static final Cache<String, Bitmap> cache = CacheBuilder
            .newBuilder()
            .maximumSize(300)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public static Optional<Bitmap> get(String url) {
        if(cache.asMap().containsKey(url)) {
            return Optional.fromNullable(cache.getIfPresent(url));
        } else {
            return Optional.absent();
        }
    }

    public static void put(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
    }

}