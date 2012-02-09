package com.kampr.models;

import android.graphics.Bitmap;

import com.forrst.api.model.Post;

public class PostDecorator {
    
    private Post post;
    private transient Bitmap userIcon;

    public Bitmap getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Bitmap userIcon) {
        this.userIcon = userIcon;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
