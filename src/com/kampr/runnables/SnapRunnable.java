package com.kampr.runnables;

import android.content.Context;
import android.graphics.Bitmap;

import com.forrst.api.model.Snap;
import com.kampr.handlers.SnapHandler;
import com.kampr.util.ImageUtils;

public class SnapRunnable extends AbstractRunnable {

    private static Bitmap _snapBitmap;
    private static Snap _snap;
    
    public SnapRunnable(Context context, SnapHandler handler, Snap snap) {
        super(context, handler);
        _snap = snap;
    }
    
    public void run() {
        _snapBitmap = ImageUtils.fetchImageBitmap(_snap.getOriginalUrl());

        if (_snapBitmap == null) {
            _snapBitmap = ImageUtils.fetchImageBitmap(_snap.getOriginalUrl());
        }

        notifyHandler();
    }
    
    public static Bitmap getSnap() {
        return _snapBitmap;
    }

}