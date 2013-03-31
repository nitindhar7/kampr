package com.nitindhar.kampr.models;

import java.io.Serializable;
import java.util.concurrent.Future;

import android.graphics.Bitmap;

import com.nitindhar.forrst.model.Post;

@SuppressWarnings("serial")
public class PostDecorator implements Serializable {

    private Post post;
    private Future<Bitmap> userIconFuture;

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

    public void setUserIconFuture(Future<Bitmap> userIconFuture) {
        this.userIconFuture = userIconFuture;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}