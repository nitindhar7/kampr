package com.nitindhar.kampr.models;

import java.io.Serializable;

import android.graphics.Bitmap;

@SuppressWarnings("serial")
public class User implements Serializable {
    
    private int _id;
    private String _username;
    private String _name;
    private String _url;
    private int _postsCount;
    private int _commentsCount;
    private int _likesCount;
    private int _followersCount;
    private int _followingCount;
    private transient Bitmap _userIcon;
    private String _bio;
    private String _role;
    private String _homepageUrl;
    private String _twitter;
    private String _tagString;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    
    public String getUsername() {
        return _username;
    }
    
    public void setUsername(String username) {
        _username = username;
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(String name) {
        _name = name;
    }
    
    public String getUrl() {
        return _url;
    }
    
    public void setUrl(String url) {
        _url = url;
    }
    
    public int getPostsCount() {
        return _postsCount;
    }
    
    public void setPostsCount(int postsCount) {
        _postsCount = postsCount;
    }
    
    public int getCommentsCount() {
        return _commentsCount;
    }
    
    public void setCommentsCount(int commentsCount) {
        _commentsCount = commentsCount;
    }
    
    public int getLikesCount() {
        return _likesCount;
    }
    
    public void setLikesCount(int likesCount) {
        _likesCount = likesCount;
    }
    
    public int getFollowersCount() {
        return _followersCount;
    }
    
    public void setFollowersCount(int followersCount) {
        _followersCount = followersCount;
    }
    
    public int getFollowingCount() {
        return _followingCount;
    }
    
    public void setFollowingCount(int followingCount) {
        _followingCount = followingCount;
    }
    
    public Bitmap getUserIcon() {
        return _userIcon;
    }
    
    public void setUserIcon(Bitmap userIcon) {
        _userIcon = userIcon;
    }
    
    public String getBio() {
        return _bio;
    }
    
    public void setBio(String bio) {
        _bio = bio;
    }
    
    public String getRole() {
        return _role;
    }
    
    public void setRole(String role) {
        _role = role;
    }
    
    public String getHomepageUrl() {
        return _homepageUrl;
    }
    
    public void setHomepageUrl(String homepageUrl) {
        _homepageUrl = homepageUrl;
    }
    
    public String getTwitter() {
        return _twitter;
    }
    
    public void setTwitter(String twitter) {
        _twitter = twitter;
    }
    
    public String getTagString() {
        return _tagString;
    }
    
    public void setTagString(String tagString) {
        _tagString = tagString;
    }
    
    @Override
    public String toString() {
        return "Id: " + _id + ", Name: " + _name + ", Username: " + _username;
    }

}
