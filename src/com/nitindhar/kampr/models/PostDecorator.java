package com.nitindhar.kampr.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import android.graphics.Bitmap;

import com.nitindhar.forrst.model.Post;

@SuppressWarnings("serial")
public class PostDecorator implements Serializable {

    private Post post;
    private Future<Bitmap> userIconFuture;
    private List<Future<Bitmap>> postSnapFutures;

    public Bitmap getUserIcon() {
        Bitmap userIcon = null;
        try {
            userIcon = userIconFuture.get();
        } catch (Exception e) {}
        return userIcon;
    }

    public Future<Bitmap> getUserIconFuture() {
        return userIconFuture;
    }

    public List<Bitmap> getPostSnaps() {
        List<Bitmap> snaps = new ArrayList<Bitmap>();
        for(Future<Bitmap> future : postSnapFutures) {
            try {
                snaps.add(future.get());
            } catch (Exception e) {}
        }
        return snaps;
    }

    public List<Future<Bitmap>> getPostSnapFutures() {
        return postSnapFutures;
    }

    public void setUserIconFuture(Future<Bitmap> userIconFuture) {
        this.userIconFuture = userIconFuture;
    }

    public void setPostSnapFutures(List<Future<Bitmap>> postSnapFutures) {
        this.postSnapFutures = postSnapFutures;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}