package com.nitindhar.kampr.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.util.ImageUtils;

public class SnapTask extends AsyncTask<Post, Integer, Bitmap> {

    private final ImageView postOriginal;

    public SnapTask(ImageView postOriginal) {
        this.postOriginal = postOriginal;
    }

    @Override
    protected Bitmap doInBackground(Post... posts) {
        Bitmap snapBitmap = ImageUtils.fetchImageBitmap(posts[0].getSnap().getOriginalUrl());
        if (snapBitmap == null) {
            snapBitmap = ImageUtils.fetchImageBitmap(posts[0].getSnap().getOriginalUrl());
        }
        return snapBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        postOriginal.setImageBitmap(bitmap);
    }

}