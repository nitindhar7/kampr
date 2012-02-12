package com.nitindhar.kampr.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {
    
    public static Bitmap getBitmapFromByteArray(byte[] imageData) {
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }
    
    public static byte[] getByteArrayFromBitmap(Bitmap imageData) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageData.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
    
    public static Bitmap fetchUserIcon(Context context, String url, int defaultDrawableId) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return getBitmapFromStream(is, context, defaultDrawableId);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: malformed URI", e);
        } catch (IOException e) {
            throw new RuntimeException("Error: could not read from stream", e);
        }
    }
    
    public static Bitmap getBitmapFromStream(InputStream is, Context context, int drawableId) {
        Bitmap bmp = BitmapFactory.decodeStream(is);
        if(bmp == null) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        }
        else {
            return bmp;
        }
    }

    public static Bitmap fetchImageBitmap(String uri) {
        Bitmap imageData = null;
        try {
            InputStream is = (InputStream) new URL(uri).getContent();
            imageData = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error processing url", e);
        } catch (IOException e) {
            throw new RuntimeException("Error fetching data from stream", e);
        }
        return imageData;
    }

}
