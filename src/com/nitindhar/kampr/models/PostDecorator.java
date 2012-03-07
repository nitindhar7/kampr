package com.nitindhar.kampr.models;

import java.io.Serializable;

import android.graphics.Bitmap;

import com.nitindhar.forrst.model.Post;

@SuppressWarnings("serial")
public class PostDecorator implements Serializable {
    
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
