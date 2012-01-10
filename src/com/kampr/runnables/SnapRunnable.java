package com.kampr.runnables;

import android.content.Context;
import android.graphics.Bitmap;

import com.kampr.handlers.SnapHandler;
import com.kampr.util.ImageUtils;

public class SnapRunnable extends AbstractRunnable {

    private static Bitmap _snap;
    private static String _snapUrl;
    
    public SnapRunnable(Context context, SnapHandler handler, String url) {
        super(context, handler);
        _snapUrl = url;
    }
    
    public void run() {
        // Attempt 1
        _snap = ImageUtils.fetchImageBitmap(_snapUrl);
        
        // Attempt 2
        if (_snap == null) {
            _snap = ImageUtils.fetchImageBitmap(_snapUrl);
        }

        notifyHandler();
    }
    
    public static Bitmap getSnap() {
        return _snap;
    }

}