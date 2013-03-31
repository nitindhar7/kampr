package com.nitindhar.kampr.async;

import java.util.concurrent.Callable;

import android.content.Context;
import android.graphics.Bitmap;

import com.nitindhar.kampr.R;
import com.nitindhar.kampr.util.ImageUtils;

public class UserIconFetchTask implements Callable<Bitmap> {

    private final Context context;
    private final String url;

    public UserIconFetchTask(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    public Bitmap call() throws Exception {
        return ImageUtils.fetchUserIcon(context, url, R.drawable.forrst_default_25);
    }

}