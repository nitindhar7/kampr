package com.kampr.models;

import java.io.Serializable;

import android.graphics.Bitmap;

@SuppressWarnings("serial")
public class Post implements Serializable {
    
    private int _id;
    private String _createdAt;
    private String _userName;
    private String _title;
    private String _url;
    private String _type;
    private String _content;
    private String _description;
    private String _snap;
    private int _viewCount;
    private int _likeCount;
    private int _commentCount;
    private transient Bitmap _userIcon;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    
    public String getCreatedAt() {
        return _createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        _createdAt = createdAt;
    }
    
    public String getUserName() {
        return _userName;
    }
    
    public void setUserName(String userName) {
        _userName = userName;
    }
    
    public String getTitle() {
        return _title;
    }
    
    public void setTitle(String title) {
        _title = title;
    }
    
    public String getUrl() {
        return _url;
    }
    
    public void setUrl(String url) {
        _url = url;
    }
    
    public String getType() {
        return _type;
    }
    
    public void setType(String type) {
        _type = type;
    }
    
    public String getContent() {
        return _content;
    }
    
    public void setContent(String content) {
        _content = content;
    }
    
    public String getDescription() {
        return _description;
    }
    
    public void setDescription(String description) {
        _description = description;
    }
    
    public int getViewCount() {
        return _viewCount;
    }
    
    public void setViewCount(int viewCount) {
        _viewCount = viewCount;
    }
    
    public int getLikeCount() {
        return _likeCount;
    }
    
    public void setLikeCount(int likeCount) {
        _likeCount = likeCount;
    }
    
    public int getCommentCount() {
        return _commentCount;
    }
    
    public void setCommentCount(int commentCount) {
        _commentCount = commentCount;
    }
    
    public Bitmap getUserIcon() {
        return _userIcon;
    }
    
    public void setUserIcon(Bitmap userIcon) {
        _userIcon = userIcon;
    }
    
    public String getSnap() {
        return _snap;
    }
    
    public void setSnap(String snap) {
        _snap = snap;
    }

}
