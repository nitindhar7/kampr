package com.nitindhar.kampr.models;

import android.graphics.Bitmap;

import com.nitindhar.forrst.model.Comment;

public class CommentDecorator {
    
    private Comment comment;
    private transient Bitmap userIcon;

    public Bitmap getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Bitmap userIcon) {
        this.userIcon = userIcon;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

}
