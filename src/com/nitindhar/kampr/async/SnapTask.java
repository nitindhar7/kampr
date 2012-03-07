package com.nitindhar.kampr.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.posts.PostActivity;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

public class SnapTask extends AsyncTask<Post, Integer, Bitmap> {

    private ImageView _postOriginal;
    
    public SnapTask(ImageView postOriginal) {
        _postOriginal = postOriginal;
    }
    
    protected Bitmap doInBackground(Post... posts) {
        Bitmap snapBitmap = ImageUtils.fetchImageBitmap(posts[0].getSnap().getOriginalUrl());
        if (snapBitmap == null) {
            snapBitmap = ImageUtils.fetchImageBitmap(posts[0].getSnap().getOriginalUrl());
        }
        return snapBitmap;
    }
    
    protected void onPostExecute(Bitmap bitmap) {
        _postOriginal.setImageBitmap(bitmap);
        LayoutUtils.layoutOverride(PostActivity.getSpinner(), View.GONE);
    }

}
