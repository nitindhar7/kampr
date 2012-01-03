package com.kampr.models;

import java.io.Serializable;

import android.graphics.Bitmap;

@SuppressWarnings("serial")
public class Comment implements Serializable {
    
    private int _id;
    private String _userName;
    private String _body;
    private String _createdAt;
    private Bitmap _userIcon;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    
    public String getUserName() {
        return _userName;
    }
    
    public void setUserName(String userName) {
        _userName = userName;
    }
    
    public String getBody() {
        return _body;
    }
    
    public void setBody(String body) {
        _body = body;
    }
    
    public String getCreatedAt() {
        return _createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        _createdAt = createdAt;
    }
    
    public Bitmap getUserIcon() {
        return _userIcon;
    }
    
    public void setUserIcon(Bitmap userIcon) {
        _userIcon = userIcon;
    }

}
