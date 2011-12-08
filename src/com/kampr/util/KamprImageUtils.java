package com.kampr.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class KamprImageUtils {
    
    public static Bitmap getBitmapFromByteArray(byte[] imageData) {
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }

}
